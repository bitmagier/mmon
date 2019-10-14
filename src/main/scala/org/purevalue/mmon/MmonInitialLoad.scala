package org.purevalue.mmon

import org.purevalue.mmon.retrieve.{AlphavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Indicators, InfluxdbPersister}
import org.slf4j.LoggerFactory

object MmonInitialLoad {
  private val log = LoggerFactory.getLogger("main")
  private val influxHostName = "localhost"
  private val influxDbName = "mmon"
  private val persister:InfluxdbPersister = new InfluxdbPersister(influxHostName, influxDbName)

  def importCompanyQuotes(): Unit = {
    val retriever:QuotesRetriever = new AlphavantageCoRetriever()
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
    try {
      importCompanyQuotes()
      applyIndicators()
    } catch {
      case t: Throwable =>
        log.error("Program failed!", t)
    }
  }
}

// TODO make importCompanyQuotes async/parallel
// TODO (for production) enable HTTPS with InfluxDB: https://docs.influxdata.com/influxdb/v1.7/administration/https_setup/