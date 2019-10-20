package org.purevalue.mmon

import org.purevalue.mmon.retrieve.{AlphavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Indicators, Influx, InfluxdbPersister}
import org.slf4j.LoggerFactory

object InitialLoad {
  private val log = LoggerFactory.getLogger("InitialLoad")
  private val persister: InfluxdbPersister = new InfluxdbPersister(Influx.influxHostName, Influx.influxDbName)

  def importCompanyQuotes(useLocalCachedData:Boolean): Unit = {
    val retriever: QuotesRetriever = new AlphavantageCoRetriever(useLocalCachedData)
    persister.dropDatabase()
    Masterdata.companies
      .filter(c => c.sector == Sector.IT || c.sector == Sector.Industrials)
      .foreach { c =>
        val ts = retriever.receiveFull(c.symbol)
        persister.write(c, ts)
        Thread.sleep(20*1000)
      }
  }

  def applyIndicators(): Unit = {
    Indicators.all.foreach(i =>
      persister.createOrReplace(i))
  }

  def initialLoad(useLocalCachedData:Boolean = false): Unit = {
    try {
      importCompanyQuotes(useLocalCachedData)
      applyIndicators()
    } catch {
      case e: Exception =>
        log.error("Program failed!", e)
        e.printStackTrace()
    }
  }
}

