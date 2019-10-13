package org.purevalue.mmon

import org.purevalue.mmon.retrieve.{AplhavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Indicators, InfluxdbPersister}

object MmonInitialLoad {
  val influxHostName = "localhost"
  val influxDbName = "mmon"
  val persister:InfluxdbPersister = new InfluxdbPersister(influxHostName, influxDbName)

  def importCompanyQuotes(): Unit = {
    val retriever:QuotesRetriever = new AplhavantageCoRetriever()
    Masterdata.companies.foreach { c =>
      val ts = retriever.receiveFull(c.symbol)
      persister.write(c, ts)
    }
  }

  def applyIndicators(): Unit = {
    Indicators.all.foreach(i =>
      persister.createOrReplace(i))
  }

  def main(args: Array[String]): Unit = {

    importCompanyQuotes()
    applyIndicators()
  }
}


// TODO (for production) enable HTTPS with InfluxDB: https://docs.influxdata.com/influxdb/v1.7/administration/https_setup/