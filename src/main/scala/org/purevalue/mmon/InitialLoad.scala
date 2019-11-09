package org.purevalue.mmon

import java.time.LocalDate

import org.purevalue.mmon.indicator.{DayValue, Indicator, Indicators}
import org.purevalue.mmon.retrieve.{AlphavantageCoRetriever, QuotesRetriever}
import org.purevalue.mmon.tsdb.{Influx, InfluxDbClient}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

object InitialLoad {
  private val log = LoggerFactory.getLogger("InitialLoad")
  private val db: InfluxDbClient = new InfluxDbClient(Influx.influxHostName, Influx.influxDbName)
  private var dataRangeFrom, dataRangeTo:LocalDate = _

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
  def addMissingDays(ts: TimeSeriesDaily): TimeSeriesDaily = {
    def generateDates(from: LocalDate, toInclusive: LocalDate): List[LocalDate] = {
      var result = List[LocalDate]()
      var decr: Int = 0
      while (!toInclusive.minusDays(decr).isBefore(from)) {
        result = toInclusive.minusDays(decr) :: result
        decr = decr + 1
      }
      result
    }

    var lastKnown: DayQuote = null
    val addedDays = ListBuffer[DayQuote]()
    for (ts <- ts.timeSeries.sorted) {
      if (lastKnown != null && lastKnown.date.plusDays(1).isBefore(ts.date)) {
        addedDays ++=
          generateDates(lastKnown.date.plusDays(1), ts.date.minusDays(1))
            .map(d => DayQuote(d, Quote(lastKnown.quote.price, 0L)))
      }
      lastKnown = ts
    }
    TimeSeriesDaily(ts.symbol, ts.timeSeries ::: addedDays.toList)
  }

  private def _importCompanyQuotes(preferLocalCachedData: Boolean): Unit = {
    def updateLoadedDataRange(ts: TimeSeriesDaily): Unit = {

      implicit val ordering: Ordering[LocalDate] = Util.localDateOrdering
      val min = ts.timeSeries.map(_.date).min
      val max = ts.timeSeries.map(_.date).max
      if (dataRangeFrom == null || min.isBefore(dataRangeFrom)) dataRangeFrom = min
      if (dataRangeTo == null || max.isAfter(dataRangeTo)) dataRangeTo = max
    }

    val retriever: QuotesRetriever = new AlphavantageCoRetriever(preferLocalCachedData = preferLocalCachedData)
    db.dropDatabase()
    Masterdata.companies
      .filter(c => c.sector == Sector.IT || c.sector == Sector.Industrials) // import is limited to these, because we have a limit of 500 API calls to alphavantage.co per day only
      .foreach { c =>
        val ts = retriever.receiveFull(c.symbol)
        val continousTs = addMissingDays(ts)
        db.writeQuotes(c, continousTs)
        updateLoadedDataRange(continousTs)
      }
  }

  private def calcIndicatorValues(i: Indicator, quotesPerCompany: Map[String, List[DayQuote]]): List[DayValue] = {
    log.info(s"Calculating indicator '${i.name}'")
    val iSymbols: Set[String] = Masterdata.companies
      .filter(c => i.companyIncluded(c))
      .map(_.symbol)
      .toSet
    val iQuotesPerCompany = quotesPerCompany.filterKeys(x => iSymbols.contains(x))
    var iValues = ListBuffer[DayValue]()

    implicit val ordering:Ordering[LocalDate] = Util.localDateOrdering
    val lastDay = iQuotesPerCompany.values.map(_.map(_.date).max).max
    var day = iQuotesPerCompany.values.map(_.map(_.date).min).min
    do {
      val prevDayData: Map[String, Quote] = iQuotesPerCompany
        .flatMap(x =>
          x._2.find(_.date == day.minusDays(1))
            .map(q => x._1 -> q.quote)
        )
      val currentDayData: Map[String, Quote] = iQuotesPerCompany
        .flatMap(x =>
          x._2.find(_.date == day)
            .map(q => x._1 -> q.quote)
        )
      iValues += DayValue(day, i.calc(prevDayData, currentDayData))

      day = day.plusDays(1)
    } while (!day.isAfter(lastDay))

    iValues.toList
  }

  private def _applyIndicators(): Unit = {
    // load in chunks of 1 year
    for (year <- dataRangeFrom.getYear to dataRangeTo.getYear) {
      _applyIndicators(
        LocalDate.ofYearDay(year, 1),
        LocalDate.ofYearDay(year+1, 1)
      )
    }
  }

  private def _applyIndicators(from:LocalDate, to:LocalDate): Unit = {
    val quotesPerCompany: Map[String, List[DayQuote]] =
      db.readQuotes(from, to)
        .groupBy(_.symbol)
        .mapValues(v =>
          v.flatMap(_.timeSeries)
            .toList
        )

    Indicators.all.foreach(i => {
      db.writeIndicator(i, calcIndicatorValues(i, quotesPerCompany))
    })
  }
}