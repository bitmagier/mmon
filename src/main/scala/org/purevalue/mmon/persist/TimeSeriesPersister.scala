package org.purevalue.mmon.persist

import org.purevalue.mmon.TimeSeriesDaily

trait TimeSeriesPersister {
  def write(stockTimeSeries: TimeSeriesDaily)
}

//class GraphitePersister(host:String, port:Int) extends Persister {
//  val GraphitePrefix = "mmon."
//  val PriceSuffix = ".price"
//  val VolumeSuffix = ".volume"
//  val graphiteClient = new GraphitePickleClient(() => new Socket(host, port))
//
//  override def write(s: StockTimeSeriesDaily): Unit = {
//    graphiteClient.writeMetrics(
//      s.timeSeries.map(
//        r => Metric(
//          GraphitePrefix+s.symbol+PriceSuffix,
//          r.time.toEpochSecond(LocalTime.of(23, 0), ZoneOffset.UTC),
//          r.price.formatted("%f")
//        )
//      )
//    )
//  }
//}
