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
      .foreach { c =>
        val ts = retriever.receiveFull(c.symbol)
        persister.write(c, ts)
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

object Main {
  private val log = LoggerFactory.getLogger("main")

  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      log.warn("Please provide action via commandline parameter.\ni = InitialLoad\nl = InitialLoad using local cached data")
    else {
      args.head match {
        case "i" => InitialLoad.initialLoad()
        case "l" => InitialLoad.initialLoad(true)
        case _ => log.error(s"Unsupported action ${args.head}")
      }
    }
  }
}

// TODO make importCompanyQuotes async/parallel
// TODO (for production) enable HTTPS with InfluxDB: https://docs.influxdata.com/influxdb/v1.7/administration/https_setup/