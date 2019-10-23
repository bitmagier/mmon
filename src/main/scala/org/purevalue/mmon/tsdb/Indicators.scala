package org.purevalue.mmon.tsdb

import org.purevalue.mmon.Sector

trait InfluxIndicator {
  def name: String
  def query: String
}

case class SectorHarmonyIndicator(sector: Sector) extends InfluxIndicator {
  override def name: String = s"sector_harmony_${sector.name}"

  override def query: String =
    s"""SELECT STDDEV("rate_of_change") as stddev
       |INTO "sector_harmony_${sector.name}"
       |FROM (
       |  SELECT DERIVATIVE("${Influx.FieldPrice}", 1d) as rate_of_change
       |  FROM "${Influx.Measurement}"
       |  WHERE "${Influx.TagSector}" = '${sector.name}'
       |  GROUP BY "${Influx.TagSymbol}"
       |) GROUP BY time(1d)""".stripMargin
}

object Indicators {
  val all: List[InfluxIndicator] = List(
    SectorHarmonyIndicator(Sector.IT),
    SectorHarmonyIndicator(Sector.Industrials)
  )
}
