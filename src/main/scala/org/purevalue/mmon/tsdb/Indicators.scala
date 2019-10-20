package org.purevalue.mmon.tsdb

import org.purevalue.mmon.Sector

trait InfluxIndicator {
  def name: String
  def query: String
}

case class SectorHarmonyIndicator(sector: Sector) extends InfluxIndicator {
  override def name: String = s"SectorHarmony${sector.name}"

  override def query: String =
    s"""SELECT STDDEV(diff)
       | INTO sectorHarmony${sector.name}
       | FROM (
       |   SELECT DIFFERENCE("${Influx.FieldPrice}") as diff
       |   FROM ${Influx.Measurement}
       |   WHERE ${Influx.TagSector} = '${sector.name}'
       | )
       |""".stripMargin
}


object Indicators {

  val all: List[InfluxIndicator] = List(
    SectorHarmonyIndicator(Sector.IT),
    SectorHarmonyIndicator(Sector.Industrials)
  )
}
