package org.purevalue.mmon

import java.net.Socket

trait Persister {
  def write(stockTimeSeries: StockTimeSeries)
}

class GraphitePersister(host:String, port:Int) extends Persister {
  val GraphitePrefix = "mmon."
  val PriceSuffix = ".price"
  val VolumeSuffix = ".volume"
  val graphiteClient = new GraphitePickleClient(() => new Socket(host, port))

  override def write(s: StockTimeSeries): Unit = {
    graphiteClient.writeMetrics(
      s.timeSeries.map(
        r => Metric(
          GraphitePrefix+s.symbol+PriceSuffix,
          r.time.toEpochSecond,
          r.avgPrice.formatted("%f")
        )
      )
    )
  }
}
