package org.purevalue.mmon.indicator

import java.time.LocalDate

import org.purevalue.mmon.{Company, Masterdata, Quote, Sector}


trait Indicator {
  /**
   * Simple company filter for this index.
   * @param company master data of a company
   * @return Yes, if company values shall be part of this index; not otherwise.
   */
  def companyIncluded(company: Company): Boolean

  /**
   * @return index name
   */
  def name: String

  /**
   * Calculates a single indicator value based on all Quote-values of the previous and the current day.
   * The indicator itself may choose if it uses both Maps or just the current one, depending on its purpuse.
   * @param previous map of all relevant quotes per company-symbol of the previous stock market day
   * @param current map of all relevant quotes per company-symbol of the current day
   * @return calculated index value for the current day
   */
  def calc(previous: Map[String, Quote], current: Map[String, Quote]): Float
}


case class DayValue(date: LocalDate, value: Float)
case class IndicatorValues(indicator: Indicator, v: List[DayValue])

/**
 *  Sector-Harmony-Indicator
 *
 * Varianz der (täglichen) Steigungsrate der Einzelwerte
 *   -> Varianz (Streuungsquadrat) = Maß für die Streuung der Wahrscheinlichkeitsdichte um ihren Schwerpunkt
 *
 *  Dies ist ein Indikator für die Harmonie (Gleichförmigkeit) der verschiedenen Änderungen des Tages,
 *  basierend auf sämtlichen Werten eines Business-Sektors.
 *
 *  Je geringer der berechnete Wert ausfällt, desto harmonischer sind die Änderungen des Tages zueinander.
 *  Größere Werten bedeuten mehr Chaos in den Änderungen.
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
  val all: Set[Indicator] =
    for (sector <- Masterdata.sectors) yield SectorHarmonyIndicator(s"SectorHarmony${sector.name}", sector)
}
