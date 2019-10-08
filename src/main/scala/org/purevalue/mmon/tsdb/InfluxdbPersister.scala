package org.purevalue.mmon.tsdb

import java.time.ZoneOffset

import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.{Company, TimeSeriesDaily}

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


    for (q <- s.timeSeries) {
      val timestamp:Long = q.time.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli
      Point(measurement, timestamp, List(
        Tag("symbol", c.symbol),
        Tag("name", c.name),
        Tag("sector", c.sector.name)
      ), List(
        DoubleField("price", q.price),
        LongField("volume", q.volume)
      ))
    }

    // TODO
    close
  }
}
