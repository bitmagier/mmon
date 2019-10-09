package org.purevalue.mmon.tsdb

import java.time.ZoneOffset

import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.{Company, DayQuote, TimeSeriesDaily}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

// https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxdbPersister extends TimeSeriesPersister {
  var influxdb: InfluxDB = _
  var db: Database = _

  influxdb.close()

  private def open(): Unit = {
    influxdb = InfluxDB.connect("localhost", 8086)
    db = influxdb.selectDatabase("my_database")
    db.exists().andThen {
      case Success(false) => db.create()
    }
  }

  private def close() = {
    db.close()
  }

  override def write(c: Company, s: TimeSeriesDaily): Unit = {
    open()
    if (!c.symbol.equals(s.symbol)) {
      throw new IllegalArgumentException()
    }

    val points = s.timeSeries.map(q => toMeasurementPoint(c,q))
    db.bulkWrite(points, Precision.HOURS, Consistency.QUORUM)

    close()
  }

  def toMeasurementPoint(c:Company, q:DayQuote): Point = {
    val timestamp: Long = q.time.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli
    Point(MarketQuotesDaily,
      timestamp,
      List(
        Tag("symbol", c.symbol),
        Tag("name", c.name),
        Tag("sector", c.sector.name)),
      List(
        DoubleField("price", q.price),
        LongField("volume", q.volume)
      ))
  }
}
