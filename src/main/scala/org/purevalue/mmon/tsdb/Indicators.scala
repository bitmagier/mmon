package org.purevalue.mmon.tsdb



case class Indicator(id:String, query:String)
object Indicators {
  val all:List[Indicator] = List(
    Indicator("sectorHarmonyTech",
      "")
  )
}
