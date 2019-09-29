package org.purevalue.mmon

import org.purevalue.mmon.indicator.Indicators
import org.purevalue.mmon.persist.InfluxdbPersister
import org.purevalue.mmon.retrieve.AplhavantageCoRetriever

object Mmon {
  def main(args: Array[String]): Unit = {
    println("starting")
    val quotesRetriever = new AplhavantageCoRetriever()
    val persister = new InfluxdbPersister()
    Indicators.all.foreach(i => {
      val memberTimeSeries:List[TimeSeriesDaily] = i.companies.map(c => )
      val indexData:TimeSeriesDaily = i.compute(memberTimeSeries)
      persister.write(indexData)
      }
    )
    println("done")
  }
}
