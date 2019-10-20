package org.purevalue.mmon.tsdb

import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.{Company, Config, DayQuote, TimeSeriesDaily}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

// https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxdbPersister(val hostName: String, val dbName: String) {
  private val log = LoggerFactory.getLogger(classOf[InfluxdbPersister])
  private val AsyncTimeout = Duration(Config.influxAsyncWriteTimeout.toMillis, TimeUnit.MILLISECONDS)
  private var influxdb: InfluxDB = _
  private var db: Database = _

  def dropDatabase():Unit = {
    try {
      open()
      Await.result(db.drop(), AsyncTimeout)
      log.info(s"Influx database '$dbName' on '$hostName' dropped")
    } finally {
      close()
    }
  }

  def write(c: Company, s: TimeSeriesDaily): Unit = {
    if (!c.symbol.equals(s.symbol)) {
      throw new IllegalArgumentException(s"Symbols mismatch! ('${c.symbol}' versus '${s.symbol}')")
    }
    log.info(s"Storing quotes for symbol '${s.symbol}' into influxdb")
    try {
      open()
      val points = s.timeSeries.map(q => toMeasurementPoint(c, q))
      if (Await.result(db.bulkWrite(points, Precision.SECONDS, Consistency.QUORUM), AsyncTimeout)) {
        log.debug(s"Quotes for symbol '${s.symbol}' successfully persisted")
      } else {
        log.warn(s"Could not persist quotes for symbol '${s.symbol}' - retrying ...") // TODO implement retry
      }
    } finally {
      close()
    }
  }

  def createOrReplace(indicator: InfluxIndicator): Unit = {
    try {
      open()
      Await.ready(
        db.exec(s"""DROP CONTINOUS QUERY ${indicator.name} on $dbName"""),
        AsyncTimeout)
      log.info(s"creating influxdb continous query for indicator '${indicator.name}'")
      Await.ready(
        db.exec(
          s"""CREATE CONTINOUS QUERY ${indicator.name} on $dbName BEGIN
           | ${indicator.query}
           | END""".stripMargin),
        AsyncTimeout)
    } finally {
      close()
    }
  }

  private def toMeasurementPoint(c: Company, q: DayQuote): Point = {
    val timestamp: Long = q.time.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
    Point(
      Influx.Measurement,
      timestamp,
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
    val exists = Await.result(db.exists(), AsyncTimeout)
    if (!exists) {
      Await.ready(db.create(), AsyncTimeout)
      log.info(s"Influx database '$dbName' on '$hostName' created")
    }
  }

  private def close(): Unit = {
    if (db != null) {
      try {
        db.close()
      } catch {
        case _:Throwable =>
      }
      db = null
    }
    if (influxdb != null) {
      try {
        influxdb.close()
      } catch {
        case _:Throwable => // ignore this
      }
      influxdb = null
    }
  }
}
