package org.purevalue.mmon.tsdb

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import com.paulgoldbaum.influxdbclient.Parameter.Precision.Precision
import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon._
import org.purevalue.mmon.indicator.{DayValue, Indicator}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

// see https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxDbClient(val hostName: String, val dbName: String) {

  private val log = LoggerFactory.getLogger(classOf[InfluxDbClient])
  private val AsyncWriteTimeout = Duration(Config.influxAsyncWriteTimeout.toMillis, TimeUnit.MILLISECONDS)
  private val AsyncReadTimeout = Duration(Config.influxAsyncReadTimeout.toMillis, TimeUnit.MILLISECONDS)
  private var influxdb: InfluxDB = _
  private var db: Database = _

  val PrecisionOfQuotes: Precision = Precision.SECONDS

  private def toQuotePoint(c: Company, q: DayQuote): Point = {
    val time: Long = q.date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
    Point(
      Influx.MeasurementQuote,
      time,
      List(
        Tag(Influx.TagSymbol, c.symbol),
        Tag(Influx.TagName, c.name),
        Tag(Influx.TagSector, c.sector.name)),
      List(
        DoubleField(Influx.FieldPrice, q.quote.price),
        DoubleField(Influx.FieldVolume, q.quote.volume)
      ))
  }


  private def toEpochSecond(date: LocalDate): Long = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)

  private def toQueryString(date: LocalDate) = s"${date.atStartOfDay(ZoneOffset.UTC).toInstant.toString}"

  private def toIndicatorPoint(i: Indicator, v: DayValue): Point = {
    val time: Long = toEpochSecond(v.date)
    Point(
      Influx.MeasurementIndicator,
      time,
      List(
        Tag(Influx.TagIndicatorName, i.name)
      ),
      List(
        DoubleField(Influx.FieldIndicatorValue, v.value)
      )
    )
  }

  private def open(): Unit = {
    influxdb = InfluxDB.connect(hostName, 8086)
    db = influxdb.selectDatabase(dbName)
    val exists = Await.result(
      db.exists(),
      AsyncWriteTimeout)
    if (!exists) {
      Await.result(
        db.create(),
        AsyncWriteTimeout
      )
      log.info(s"Influx database '$dbName' on '$hostName' created")
    }
  }

  private def close(): Unit = {
    if (db != null) {
      try {
        db.close()
      } catch {
        case _: Throwable =>
      }
      db = null
    }
    if (influxdb != null) {
      try {
        influxdb.close()
      } catch {
        case _: Throwable => // ignore this
      }
      influxdb = null
    }
  }

  def dropDatabase(): Unit = {
    try {
      open()
      Await.result(
        db.drop(),
        AsyncWriteTimeout)
      log.info(s"Influx database '$dbName' on '$hostName' dropped")
    } finally {
      close()
    }
  }

  def toString(queryResult: QueryResult): String = {
    val sb = StringBuilder.newBuilder
    sb.append("query result:\n")
    queryResult.series.foreach(s => {
      sb.append(s.columns.mkString(" | ")).append("\n")
      s.records.foreach(r => {
        sb.append(r.allValues.mkString(", "))
        sb.append("\n")
      })
      sb.append("---------------\n")
    })
    sb.mkString
  }

  private def singleRowTimeQuery(query: String): LocalDate = {
    try {
      open()
      log.debug(query)
      val queryResult: QueryResult = Await.result(
        db.query(query, PrecisionOfQuotes),
        AsyncReadTimeout)
      log.debug(toString(queryResult))
      queryResult.series.head.records.head("time") match {
        case t: BigDecimal => LocalDateTime.ofEpochSecond(t.toLongExact, 0, ZoneOffset.UTC).toLocalDate
      }
    } finally {
      close()
    }
  }

  def queryQuotesMinDate(): LocalDate = singleRowTimeQuery(s"select first(${Influx.FieldPrice}), time from ${Influx.MeasurementQuote}")

  def queryQuotesMaxDate(): LocalDate = singleRowTimeQuery(s"select last(${Influx.FieldPrice}), time from ${Influx.MeasurementQuote}")


  private def fromQuoteSeries(s: Series): Iterable[TimeSeriesDaily] = {
    val timeSeriesPerSymbol: Map[String, List[(String, DayQuote)]] =
      s.records.map(r => {
        val symbol = r(s"${Influx.TagSymbol}") match {
          case s: String => s
        }
        val date: LocalDate = r("time") match {
          case t: BigDecimal => LocalDateTime.ofEpochSecond(t.toLongExact, 0, ZoneOffset.UTC).toLocalDate
        }
        val price = r(s"${Influx.FieldPrice}") match {
          case p: BigDecimal => p.toFloat
        }
        val volume = r(s"${Influx.FieldVolume}") match {
          case v: BigDecimal => v.toLongExact
        }
        (symbol, DayQuote(date, Quote(price, volume)))
      }).groupBy(_._1)
    timeSeriesPerSymbol.map(x => TimeSeriesDaily(x._1, x._2.map(_._2)))
  }

  def readQuotes(from: LocalDate, to: LocalDate): Iterable[TimeSeriesDaily] = {
    try {
      open()
      val query = s"select * from ${Influx.MeasurementQuote} where time >= '${toQueryString(from)}' and time <= '${toQueryString(to)}'"
      log.debug(query)
      val queryResult: QueryResult = Await.result(
        db.query(query, PrecisionOfQuotes),
        AsyncReadTimeout)
      assert(queryResult.series.size == 1)
      queryResult.series.flatMap(fromQuoteSeries)
    } finally {
      close()
    }
  }

  def writeQuotes(c: Company, s: TimeSeriesDaily): Unit = {
    if (!c.symbol.equals(s.symbol)) {
      throw new IllegalArgumentException(s"SelfCheck: Symbols mismatch! ('${c.symbol}' versus '${s.symbol}')")
    }
    try {
      open()
      log.info(s"Writing quotes for symbol '${s.symbol}' into influxdb")
      val points = s.timeSeries.map(q => toQuotePoint(c, q))
      if (Await.result(
        db.bulkWrite(points, PrecisionOfQuotes, Consistency.QUORUM),
        AsyncWriteTimeout)) {
        log.debug(s"Quotes for symbol '${s.symbol}' successfully written")
      } else {
        log.warn(s"Could not write quotes for symbol '${s.symbol}'. Could implement a retry for that ;-)")
      }
    } finally {
      close()
    }
  }

  def logInvalidValues(i:Indicator, values: List[DayValue], invalidsFilter: DayValue => Boolean):Unit = {
    val invalids = values.filter(invalidsFilter)
    if (invalids.size == 1) {
      log.warn(s"Found invalid value (${invalids.head}) for indicator ${i.name}")
    } else if (invalids.size > 1) {
      log.warn(s"Found ${invalids.size} values for indicator ${i.name}")
    }
  }

  def writeIndicator(i: Indicator, values: List[DayValue]): Unit = {
    try {
      open()
      val invalidsFilter: (DayValue) => Boolean = { v => v.value.isNaN || v.value.isInfinity }
      logInvalidValues(i, values, invalidsFilter)
      log.info(s"Writing indicator '${i.name}' into influxdb")
      val points = values
        .filterNot(invalidsFilter)
        .map(v => toIndicatorPoint(i, v))
      if (Await.result(
        db.bulkWrite(points, PrecisionOfQuotes, Consistency.QUORUM),
        AsyncWriteTimeout)) {
        log.debug(s"Indicator '${i.name}' written")
      } else {
        log.warn(s"Could not write indicator")
      }
    } finally {
      close()
    }
  }
}
