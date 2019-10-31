package org.purevalue.mmon.indicator

import java.lang.Math.sqrt

import org.purevalue.mmon.{Company, DayQuote, Sector}


trait Indicator {
  type CalcBaseSingleDay = List[DayQuote]

  def companyFilter(company: Company): Boolean

  def name: String

  def calc(previous: CalcBaseSingleDay, current: CalcBaseSingleDay): Float
}

/**
 * Varianz der (täglichen) Steigungsrate der einzelnen Werte
 *
 * Varianz (Streuungsquadrat) : Maß für die Streuung der Wahrscheinlichkeitsdichte um ihren Schwerpunkt
 *
 */
private[indicator]
case class SectorHarmonyIndicator(name: String, sector: Sector) extends Indicator {
  override def companyFilter(company: Company): Boolean = (company.sector == this.sector)

  override def calc(previous: CalcBaseSingleDay, current: CalcBaseSingleDay): Float = {
    val changeRate: List[Double] =
      previous.zip(current)
        .map(x => (x._2.price.toDouble - x._1.price) / x._1.price) // percentual change of the day
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