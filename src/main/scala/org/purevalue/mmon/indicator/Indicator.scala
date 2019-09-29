package org.purevalue.mmon.indicator

import java.time.LocalDate

import org.purevalue.mmon.{Company, DayQuote, Masterdata, ReportingGroup, TimeSeriesDaily}

import scala.collection.immutable

abstract class Indicator {
  def name:String
  def reportingGroup:ReportingGroup
  def compute(memberTimeSeries:List[TimeSeriesDaily]):TimeSeriesDaily
}

case class UnweightedSumIndicator(name:String, reportingGroup:ReportingGroup) extends Indicator {

  def calcRate(quotes: immutable.Iterable[DayQuote]): DayQuote = {
    val times = quotes.groupBy(_.time).keys
    if (times.size > 1) {
      throw new IllegalArgumentException("Quotes are not from the same time")
    }
    val price = quotes.foldLeft(0.0f)((a,quote) => a + quote.price)
    DayQuote(times.head, price, null)
  }

  override def compute(memberTimeSeries: List[TimeSeriesDaily]): List[TimeSeriesDaily] = {
    val valuesPerDay = Map[LocalDate, Map[Company, DayQuote]]()
    // TODO fill ^^^

    val index: List[DayQuote] = valuesPerDay
      .map(v => DayQuote(v._1, calcRate(v._2.map(_._2))))

    TimeSeriesDaily(name, index)
  }
}
case class SectorHarmonyIndicator(name:String, reportingGroup: ReportingGroup) extends Indicator {
  override def compute(memberTimeSeries:List[TimeSeriesDaily]):List[TimeSeriesDaily] = {

  }
}

object ReportingGroups {
  val IT = ReportingGroup("IT", Masterdata.companies().filter(_.sector.equals("Information Technology")))
}

object Indicators {

  def all: List[Indicator] = List(
    UnweightedSumIndicator("Sum:IT", ReportingGroups.IT),
    SectorHarmonyIndicator("SectorHarmony:IT", ReportingGroups.IT)
    )
}