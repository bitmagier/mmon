package org.purevalue.mmon.indicator

import java.time.LocalDate

import org.purevalue.mmon.{Company, Quote, Sector}


trait Indicator {
  def companyIncluded(company: Company): Boolean
  def name: String
  def calc(previous: Map[String, Quote], current: Map[String, Quote]): Float
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

  override def calc(previous: Map[String, Quote], current: Map[String, Quote]): Float = {
    val changeRate: List[Double] = current
      .filter(e => previous.get(e._1).isDefined)
      .map(e => (previous(e._1), e._2))
      .map(tuple => (tuple._2.price.toDouble - tuple._1.price) / tuple._1.price) // percentual change of the day
      .toList
    variance(changeRate).toFloat
  }

  private def variance(values: List[Double]): Double = {
    val arithmeticMiddle = values.sum / values.length
    values
      .map(r => Math.pow(arithmeticMiddle - r, 2.0))
      .sum / values.length
  }
}


object Indicators {
  val all: Seq[Indicator] = List(
    SectorHarmonyIndicator("SectorHarmonyIT", Sector.IT),
    SectorHarmonyIndicator("SectorHarmonyIndustrials", Sector.Industrials)
  )
}
