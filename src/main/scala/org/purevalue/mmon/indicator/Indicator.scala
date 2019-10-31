package org.purevalue.mmon.indicator

import java.lang.Math.sqrt
import java.time.LocalDate

import org.purevalue.mmon.{Company, Quote, Sector}


trait Indicator {
  type SingleDayCompanyQuotes = Map[String, Quote]

  def companyIncluded(company: Company): Boolean

  def name: String

  def calc(previous: SingleDayCompanyQuotes, current: SingleDayCompanyQuotes): Float
}


case class DayValue(date: LocalDate, value: Float)

case class IndicatorValues(indicator: Indicator, v: List[DayValue])

/**
 * Varianz der (täglichen) Steigungsrate der einzelnen Werte
 *
 * Varianz (Streuungsquadrat) : Maß für die Streuung der Wahrscheinlichkeitsdichte um ihren Schwerpunkt
 *
 */
private[indicator]
case class SectorHarmonyIndicator(name: String, sector: Sector) extends Indicator {
  override def companyIncluded(company: Company): Boolean = (company.sector == this.sector)

  override def calc(previous: SingleDayCompanyQuotes, current: SingleDayCompanyQuotes): Float = {
    val changeRate: List[Double] =
      current.map(e => (previous(e._1), e._2))
        .map(x => (x._2.price.toDouble - x._1.price) / x._1.price) // percentual change of the day
        .toList
    variance(changeRate).toFloat
  }

  private def variance(values: List[Double]): Double = {
    val arithmeticMiddle = values.sum / values.length
    values
      .map(r => sqrt(arithmeticMiddle - r))
      .sum / values.length
  }
}


object Indicators {
  val all: Seq[Indicator] = List(
    SectorHarmonyIndicator("SectorHarmonyIT", Sector.IT),
    SectorHarmonyIndicator("SectorHarmonyIndustrials", Sector.Industrials)
  )
}
