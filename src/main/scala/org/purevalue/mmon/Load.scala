package org.purevalue.mmon

import java.time.LocalDate

import org.purevalue.mmon.indicator.{DayValue, Indicator, Indicators}
import org.purevalue.mmon.retrieve.Retriever
import org.purevalue.mmon.retrieve.quotes.alphavantage.{AlphavantageCoRetriever, UnknownSymbolException}
import org.purevalue.mmon.tsdb.{Influx, InfluxDbClient}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

object Load {
  private val log = LoggerFactory.getLogger("org.purevalue.mmon.InitialLoad")
  private val db: InfluxDbClient = new InfluxDbClient(Influx.influxHostName, Influx.influxDbName)

  def initialLoad(preferLocalCachedData: Boolean = true, dropDatabase: Boolean = true): Unit = {
    try {
      _importCompanyQuotes(preferLocalCachedData, dropDatabase)
      _applyIndicators()
    } catch {
      case e: Throwable =>
        log.error("InitialLoad failed!", e)
    }
  }

  /* entry point for indicator debugging purposes */
  def applyIndicators(): Unit = {
    try {
      _applyIndicators()
    } catch {
      case e: Throwable =>
        log.error("ApplyIndicators failed!", e)
    }
  }

  private def companyFilter = (c: Company) => {
    Config.dataBusinessSectorFilter.isEmpty ||
      Config.dataBusinessSectorFilter.contains(c.sector.name)
  }

  private def _importCompanyQuotes(preferLocalCachedData: Boolean, dropDatabase: Boolean): Unit = {
    val retriever: Retriever = new AlphavantageCoRetriever(preferLocalCachedData = preferLocalCachedData)
    if (dropDatabase) db.dropDatabase()
    var missingCompanyCounter: Int = 0
    Masterdata.companies
      .filter(companyFilter)
      .foreach { c =>
        try {
          val ts = retriever.retrieveFull(c.symbol)
          //          val continuousTs = addMissingDays(ts)
          db.writeQuotes(c, ts)
        } catch {
          case UnknownSymbolException(_) => {
            log.warn(s"Quotes for company symbol $c not found")
            missingCompanyCounter += 1
            if (missingCompanyCounter > Config.maxMissingCompanies) {
              throw new Exception(s"Exceeded limit (${Config.maxMissingCompanies}) of max missing companies")
            }
          }
        }
      }
  }

  private def filterQuotesOfDay(quotes: Map[String, List[DayQuote]], day: LocalDate): Map[String, Quote] = {
    quotes.flatMap(x =>
      x._2.find(_.date == day)
        .map(q => x._1 -> q.quote)
    )
  }

  /**
   * @param indicator Indicator (definition) to calculate
   * @param quotes relevant quotes for indicator
   * @return calculated indicator values
   */
  private[mmon] def calcIndicator(indicator: Indicator, quotes: Map[String, List[DayQuote]]): List[DayValue] = {
    if (quotes.isEmpty) {
      List[DayValue]()
    } else {
      implicit val localDateOrdering: Ordering[LocalDate] = Util.localDateOrdering
      val veryLastDayOfData: LocalDate = quotes.values
        .map(_.map(_.date).max)
        .max
      var day: LocalDate = quotes.values
        .map(_.map(_.date).min)
        .min

      log.info(s"Calculating indicator '${indicator.name}' from $day to $veryLastDayOfData")

      val allDaySet: Set[LocalDate] = quotes.flatMap(_._2.map(_.date)).toSet // all days with quotes (as a set)
      val indicatorValues = ListBuffer[DayValue]()

      while (day.isBefore(veryLastDayOfData)) {
        val prevDay = day
        do {
          day = day.plusDays(1)
        } while (!(day.isAfter(veryLastDayOfData) || allDaySet.contains(day)))
        if (allDaySet.contains(day)) {
          val precedingDayQuotes: Map[String, Quote] = filterQuotesOfDay(quotes, prevDay)
          val dayQuotes: Map[String, Quote] = filterQuotesOfDay(quotes, day)
          val iValue = indicator.calc(precedingDayQuotes, dayQuotes)
          indicatorValues += DayValue(day, iValue)
        }
      }
      indicatorValues.toList
    }
  }

  // calculates the values of the indicator based on a (to be filtered) set of company quotes
  private[mmon] def calcIndcatorGivenFullQuotesDataset(i: Indicator, quotesPerCompany: Map[String, List[DayQuote]]): List[DayValue] = {
    val iSymbols: Set[String] = Masterdata.companies
      .filter(c => i.companyIncluded(c))
      .map(_.symbol)
      .toSet
    val relevantQuotes = quotesPerCompany.filterKeys(x => iSymbols.contains(x))
    calcIndicator(i: Indicator, relevantQuotes: Map[String, List[DayQuote]])
  }

  private def _applyIndicators(): Unit = {
    // load in chunks of 1 year, to prevent OutOfMemory/Timeout
    val dataRangeFrom = db.queryQuotesMinDate()
    val dataRangeTo = db.queryQuotesMaxDate()
    for (year <- dataRangeFrom.getYear to dataRangeTo.getYear) {
      _applyIndicators(
        LocalDate.ofYearDay(year, 1),
        LocalDate.ofYearDay(year + 1, 1)
      )
    }
  }

  private def _applyIndicators(from: LocalDate, to: LocalDate): Unit = {
    val quotesPerCompany: Map[String, List[DayQuote]] =
      db.readQuotes(from, to)
        .groupBy(_.symbol)
        .mapValues(v => v.flatMap(_.timeSeries).toList)

    Indicators.all.foreach(i => {
      val indicatorValues = calcIndcatorGivenFullQuotesDataset(i, quotesPerCompany)
      if (indicatorValues.nonEmpty) {
        db.writeIndicator(i, indicatorValues)
      }
    })
  }
}
