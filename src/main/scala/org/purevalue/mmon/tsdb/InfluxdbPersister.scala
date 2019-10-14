package org.purevalue.mmon.tsdb

import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.{Company, DayQuote, TimeSeriesDaily}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

// https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxdbPersister(val hostName: String, val dbName: String) {
  private val log = LoggerFactory.getLogger(classOf[InfluxdbPersister])
  private val AsyncTimeout: Duration = Duration(1, TimeUnit.SECONDS)
  private var influxdb: InfluxDB = _
  private var db: Database = _

  def write(c: Company, s: TimeSeriesDaily): Unit = {
    if (!c.symbol.equals(s.symbol)) {
      throw new IllegalArgumentException("Symbols mismatch!")
    }
    log.info(s"Storing quotes for symbol '${}' into influxdb")
    try {
      open()
      val points = s.timeSeries.map(q => toMeasurementPoint(c, q))
      db.bulkWrite(points, Precision.HOURS, Consistency.QUORUM)
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
    val timestamp: Long = q.time.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli
    Point(
      Influx.Measurement,
      timestamp,
      List(
        Tag(Influx.TagSymbol, c.symbol),
        Tag(Influx.TagName, c.name),
        Tag(Influx.TagSector, c.sector.name)),
      List(
        DoubleField(Influx.FieldPrice, q.price),
        LongField(Influx.FieldVolume, q.volume)
      ))
  }

  private def open(): Unit = {
    influxdb = InfluxDB.connect(hostName, 8086)
    db = influxdb.selectDatabase(dbName)
    val exists = Await.result(db.exists(), AsyncTimeout)
    if (!exists) {
      Await.ready(db.create(), AsyncTimeout)
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
        case _:Throwable =>
      }
      influxdb = null
    }
  }
}
