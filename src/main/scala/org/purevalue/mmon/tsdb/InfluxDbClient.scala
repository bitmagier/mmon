package org.purevalue.mmon.tsdb

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import com.paulgoldbaum.influxdbclient.Parameter.Precision.Precision
import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.{Company, Config, DayQuote, TimeSeriesDaily}
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


  //  def createOrReplace(indicator: InfluxIndicator): Unit = {
  //    try {
  //      open()
  //      Await.ready(
  //        db.exec(s"""DROP CONTINOUS QUERY ${indicator.name} on $dbName"""),
  //        AsyncTimeout)
  //
  //      val influxQl =
  //        s"""CREATE CONTINUOUS QUERY "${indicator.name}" on "$dbName"
  //           |RESAMPLE EVERY 2h
  //           |BEGIN
  //           |${indicator.query}
  //           |END""".stripMargin
  //      log.info(s"creating influxdb continous query for indicator '${indicator.name}'\n$influxQl")
  //      val result: QueryResult = Await.result(db.exec(influxQl), AsyncTimeout)
  //      log.info(result.toString)
  //    } finally {
  //      close()
  //    }
  //  }

  private def toPoint(c: Company, q: DayQuote): Point = {
    val time: Long = q.date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
    Point(
      Influx.MeasurementQuote,
      time,
      List(
        Tag(Influx.TagSymbol, c.symbol),
        Tag(Influx.TagName, c.name),
        Tag(Influx.TagSector, c.sector.name)),
      List(
        DoubleField(Influx.FieldPrice, q.price),
        DoubleField(Influx.FieldVolume, q.volume)
      ))
  }

  private def open(): Unit = {
    influxdb = InfluxDB.connect(hostName, 8086)
    db = influxdb.selectDatabase(dbName)
    val exists = Await.result(db.exists(), AsyncWriteTimeout)
    if (!exists) {
      Await.ready(
        db.create(),
        AsyncWriteTimeout)
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
        (symbol, DayQuote(date, price, volume))
      }).groupBy(_._1)
    timeSeriesPerSymbol.map(x => TimeSeriesDaily(x._1, x._2.map(_._2)))
  }

  def readQuotes(): Iterable[TimeSeriesDaily] = {
    try {
      open()
      val queryResult: QueryResult = Await.result(
        db.query(s"select * from ${Influx.MeasurementQuote}", PrecisionOfQuotes),
        AsyncReadTimeout)
      assert(queryResult.series.size == 1)
      queryResult.series.flatMap(fromQuoteSeries)
    } finally {
      close()
    }
  }

  def write(c: Company, s: TimeSeriesDaily): Unit = {
    if (!c.symbol.equals(s.symbol)) {
      throw new IllegalArgumentException(s"SelfCheck: Symbols mismatch! ('${c.symbol}' versus '${s.symbol}')")
    }
    log.info(s"Storing quotes for symbol '${s.symbol}' into influxdb")
    try {
      open()
      val points = s.timeSeries.map(q => toPoint(c, q))
      if (Await.result(
        db.bulkWrite(points, PrecisionOfQuotes, Consistency.QUORUM),
        AsyncWriteTimeout)) {
        log.debug(s"Quotes for symbol '${s.symbol}' successfully persisted")
      } else {
        log.warn(s"Could not persist quotes for symbol '${s.symbol}'. Could implement a retry for that ;-)")
      }
    } finally {
      close()
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
}
