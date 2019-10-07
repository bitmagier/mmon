package org.purevalue.mmon.tsdb

import org.purevalue.mmon.Sector


case class Indicator(id:String, query:String)

object Indicators {
  val all:List[Indicator] = List(
    Indicator("sectorHarmonyTech",
      "select STDDEV() from ( " +
        "select DIFFERENCE( ".*quote") where sector = '" + Sector.IT + "'"
        " )")
  )
}
