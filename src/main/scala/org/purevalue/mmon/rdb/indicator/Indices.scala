package org.purevalue.mmon.rdb.indicator

import org.purevalue.mmon.TimeSeriesDaily

trait Index {
  def name:String
  def calculate(): TimeSeriesDaily
}

case class SectorHarmonyIndex() extends Index {
  val name = "SectorHarmony"

  override def calculate(): TimeSeriesDaily = {
    // TODO
  }
}

object Indices {
  val all = List(
    SectorHarmonyIndex()
  )
}
