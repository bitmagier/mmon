package org.purevalue.mmon

import org.purevalue.mmon.retrieve.{AplhavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{InfluxdbPersister, TimeSeriesPersister}

object MmonInitialLoad {

  def importCompanyQuotes(): Unit = {
    val retriever:QuotesRetriever = new AplhavantageCoRetriever()
    val persister:TimeSeriesPersister = new InfluxdbPersister()

    Masterdata.companies.foreach { c =>
      val ts = retriever.receiveFull(c.symbol)
      persister.write(c, ts)
    }
  }

  def main(args: Array[String]): Unit = {
//    createTables()
    importCompanyQuotes()
//    applyIndicators()
  }
}
