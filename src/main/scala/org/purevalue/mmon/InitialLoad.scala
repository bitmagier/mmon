package org.purevalue.mmon

import java.time.LocalDate

import org.purevalue.mmon.indicator.{DayValue, Indicators}
import org.purevalue.mmon.retrieve.{AlphavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Influx, InfluxDbClient}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

object InitialLoad {
  private val log = LoggerFactory.getLogger("InitialLoad")
  private val db: InfluxDbClient = new InfluxDbClient(Influx.influxHostName, Influx.influxDbName)

  def initialLoad(preferLocalCachedData: Boolean = false): Unit = {
    try {
      _importCompanyQuotes(preferLocalCachedData)
      _applyIndicators()
    } catch {
      case e: Throwable =>
        log.error("InitialLoad failed!", e)
    }
  }

  def applyIndicators(): Unit = {
    try {
      _applyIndicators()
    } catch {
      case e: Throwable =>
        log.error("ApplyIndicators failed!", e)
    }
  }

  /** adds entries for gaps in the data - using the value from the last known day before */
  def fillMissingDays(ts: TimeSeriesDaily): TimeSeriesDaily = {
    // TODO
  }

  private def _importCompanyQuotes(preferLocalCachedData: Boolean): Unit = {
    val retriever: QuotesRetriever = new AlphavantageCoRetriever(preferLocalCachedData = preferLocalCachedData)
    db.dropDatabase()
    Masterdata.companies
      .filter(c => c.sector == Sector.IT || c.sector == Sector.Industrials) // import is limited to these, because we have a limit of 500 API calls to alphavantage.co per day only
      .foreach { c =>
        val ts = retriever.receiveFull(c.symbol)
        val continousTs = fillMissingDays(ts)
        db.writeQuotes(c, continousTs)
      }

  }

  private def _applyIndicators(): Unit = {
    val companiesBySymbol: Map[String, Company] = Masterdata.companies.map(c => c.symbol -> c).toMap
    val sortedQuotesByCompany: Map[String, List[DayQuote]] =
      db.readQuotes()
        .groupBy(_.symbol)
        .mapValues(v =>
          v.flatMap(_.timeSeries)
            .toList
            .sorted
        )

    Indicators.all.foreach(i => {
      log.info(s"Calculating indicator '${i.name}'")
      val symbols: Set[String] = Masterdata.companies
        .filter(c => i.companyIncluded(c))
        .map(_.symbol)
        .toSet

      var indicatorValues = ListBuffer[DayValue]()
      val days:List[LocalDate] = // TODO

      for (day <- days) {
        val prevDayData:Map[String,Quote] = sortedQuotesByCompany
          .filterKeys(x => symbols.contains(x))
          .flatMap(x =>
            x._2.find(_.date == day.minusDays(1))
              .map(q => x._1 -> q.quote)
          )
        val currentDayData:Map[String,Quote] = sortedQuotesByCompany
          .filterKeys(x => symbols.contains(x))
          .flatMap(x =>
            x._2.find(_.date == day)
            .map(q => x._1 -> q.quote)
          )

        indicatorValues += DayValue (day, i.calc(prevDayData, currentDayData))
      }

      db.writeIndicator(i, indicatorValues.toList)
    })
  }
}