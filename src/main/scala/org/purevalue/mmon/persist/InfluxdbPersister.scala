package org.purevalue.mmon.persist
import org.purevalue.mmon.retrieve.StockTimeSeriesDaily
import com.paulgoldbaum.influxdbclient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

// https://docs.influxdata.com/influxdb/v1.7/concepts/key_concepts/
class InfluxdbPersister extends Persister {
  var influxdb: InfluxDB
  var db: Database

  influxdb.close()



  def open(): Unit = {
    influxdb = InfluxDB.connect("localhost", 8086)
    db = influxdb.selectDatabase("my_database")
    db.exists().andThen {
      case Success(false) => db.create()
    }
  }

  def close() = {
    db.close()
  }

  override def write(s: StockTimeSeriesDaily): Unit = {
    open()
    Point("quote_"+s.symbol)
    close()
  }
}
