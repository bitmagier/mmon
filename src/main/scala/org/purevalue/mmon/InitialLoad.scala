package org.purevalue.mmon

import org.purevalue.mmon.retrieve.{AlphavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Indicators, Influx, InfluxdbPersister}
import org.slf4j.LoggerFactory

object InitialLoad {
  private val log = LoggerFactory.getLogger("InitialLoad")
  private val persister: InfluxdbPersister = new InfluxdbPersister(Influx.influxHostName, Influx.influxDbName)

  def importCompanyQuotes(preferLocalCachedData: Boolean): Unit = {
    try {
      val retriever: QuotesRetriever = new AlphavantageCoRetriever(preferLocalCachedData = preferLocalCachedData)
      persister.dropDatabase()
      Masterdata.companies
        .filter(c => c.sector == Sector.IT || c.sector == Sector.Industrials) // import is limited to these, because we have a limit of 500 API calls to alphavantage.co per day only
        .foreach { c =>
          val ts = retriever.receiveFull(c.symbol)
          persister.write(c, ts)
        }
    } catch {
      case e: Exception =>
        log.error("Import of quotes failed!", e)
        throw e
    }
  }

  def applyIndicators(): Unit = {
    try {
      Indicators.all.foreach(i =>
        persister.createOrReplace(i))
    } catch {
      case e: Exception =>
        log.error("Apply indicators failed!", e)
        throw e
    }
  }

  def initialLoad(preferLocalCachedData: Boolean = false): Unit = {
    importCompanyQuotes(preferLocalCachedData)
    applyIndicators()
  }
}