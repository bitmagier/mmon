package org.purevalue.mmon.persist
import com.paulgoldbaum.influxdbclient._
import org.purevalue.mmon.TimeSeriesDaily

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

// https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxdbPersister extends TimeSeriesPersister {
  var influxdb: InfluxDB = _
  var db: Database = _

  influxdb.close()

  private def open: Unit = {
    influxdb = InfluxDB.connect("localhost", 8086)
    db = influxdb.selectDatabase("my_database")
    db.exists().andThen {
      case Success(false) => db.create()
    }
  }

  private def close = {
    db.close()
  }

  override def write(s: TimeSeriesDaily): Unit = {
    open
    Point(s.symbol)
    // TODO
    close
  }
}
