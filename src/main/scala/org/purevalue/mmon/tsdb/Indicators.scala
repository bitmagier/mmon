package org.purevalue.mmon.tsdb

import org.purevalue.mmon.Sector


case class Indicator(id:String, query:String)

object Indicators {

  val SectorHarmonyTechIndicator = Indicator("SectorHarmonyTech",
        s"""select STDDEV(...) from (select DIFFERENCE(".*quote"))""")


  val all:List[Indicator] = List(
    // TODO
//    Indicator("sectorHarmonyTech",
//      "select STDDEV() from ( " +
//        "select DIFFERENCE( ".*quote") where sector = '" + Sector.IT + "'"
//        " )")

  )
}
