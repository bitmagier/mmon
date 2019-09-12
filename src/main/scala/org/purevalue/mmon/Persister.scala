package org.purevalue.mmon

trait Persister {
  def save(stockTimeSeries: StockTimeSeries)
}

class GraphitePersister(host:String, port:Int) extends Persister {
  val GraphitePrefix: String = "mmon_"

  override def save(stockTimeSeries: StockTimeSeries): Unit = {
    // TODO
  }
}
