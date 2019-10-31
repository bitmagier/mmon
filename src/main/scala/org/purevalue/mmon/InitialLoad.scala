package org.purevalue.mmon

import org.purevalue.mmon.retrieve.{AlphavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Influx, InfluxDbClient}
import org.slf4j.LoggerFactory

object InitialLoad {
  private val log = LoggerFactory.getLogger("InitialLoad")
  private val persister: InfluxDbClient = new InfluxDbClient(Influx.influxHostName, Influx.influxDbName)

  def initialLoad(preferLocalCachedData: Boolean = false): Unit = {
    try {
      _importCompanyQuotes(preferLocalCachedData)
//      _applyIndicators()
    } catch {
      case e: Throwable =>
        log.error("InitialLoad failed!", e)
    }
  }

//  def applyIndicators(): Unit = {
//    try {
//      _applyIndicators()
//    } catch {
//      case e: Throwable =>
//        log.error("ApplyIndicators failed!", e)
//    }
//  }

  private def _importCompanyQuotes(preferLocalCachedData: Boolean): Unit = {
    val retriever: QuotesRetriever = new AlphavantageCoRetriever(preferLocalCachedData = preferLocalCachedData)
    persister.dropDatabase()
    Masterdata.companies
      .filter(c => c.sector == Sector.IT || c.sector == Sector.Industrials) // import is limited to these, because we have a limit of 500 API calls to alphavantage.co per day only
      .foreach { c =>
        val ts = retriever.receiveFull(c.symbol)
        persister.write(c, ts)
      }

  }

//  private def _applyIndicators(): Unit = {
//    InfluxIndicators.all.foreach(i =>
//      persister.createOrReplace(i))
//  }
}