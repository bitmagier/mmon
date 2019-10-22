package org.purevalue.mmon.tsdb

import org.purevalue.mmon.Sector

trait InfluxIndicator {
  def name: String
  def query: String
}

case class SectorHarmonyIndicator(sector: Sector) extends InfluxIndicator {
  override def name: String = s"sector_harmony_${sector.name}"

  override def query: String =
    s"""SELECT STDDEV(diff) as stddev
       |INTO "sector_harmony_${sector.name}"
       |FROM (
       |  SELECT DIFFERENCE("${Influx.FieldPrice}") as diff
       |  FROM "${Influx.Measurement}"
       |  WHERE ${Influx.TagSector} = '${sector.name}'
       |) group by time(1d)""".stripMargin
}
// TODO Server answered with error code 400. Message: {"error":"error parsing query: found ), expected GROUP BY time(...) at line 10, char 2"}


object Indicators {
  val all: List[InfluxIndicator] = List(
    SectorHarmonyIndicator(Sector.IT),
    SectorHarmonyIndicator(Sector.Industrials)
  )
}
