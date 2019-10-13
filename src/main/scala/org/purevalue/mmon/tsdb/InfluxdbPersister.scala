package org.purevalue.mmon.tsdb

import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.{Company, DayQuote, TimeSeriesDaily}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

// https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxdbPersister(val hostName: String, val dbName: String) {

  val AsyncTimeout: Duration = Duration(1, TimeUnit.SECONDS)

  private var influxdb: InfluxDB = _
  private var db: Database = _

  def write(c: Company, s: TimeSeriesDaily): Unit = {
    open()
    if (!c.symbol.equals(s.symbol)) {
      throw new IllegalArgumentException()
    }

    val points = s.timeSeries.map(q => toMeasurementPoint(c, q))
    db.bulkWrite(points, Precision.HOURS, Consistency.QUORUM)
    close()
  }


  def createOrReplace(indicator: InfluxIndicator): Unit = {
    open()
    Await.ready(db.exec(s"""DROP CONTINOUS QUERY ${indicator.name} on $dbName"""), AsyncTimeout)
    Await.ready(db.exec(
      s"""CREATE CONTINOUS QUERY ${indicator.name} on $dbName BEGIN
         | ${indicator.query}
         | END""".stripMargin), AsyncTimeout)
    close()
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
    db.close()
    influxdb.close()
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
}
