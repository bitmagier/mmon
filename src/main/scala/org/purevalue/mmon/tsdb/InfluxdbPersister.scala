package org.purevalue.mmon.tsdb

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

    val fieldNames = List("quote", "volume")
    val tagNames = List("symbol", "isin", "wkn", "name", "sector")

    val timestamp =

      Point()
    // TODO
    close
  }
}
