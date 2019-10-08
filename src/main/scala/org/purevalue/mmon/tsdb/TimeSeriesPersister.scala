package org.purevalue.mmon.tsdb

import org.purevalue.mmon.{Company, TimeSeriesDaily}

trait TimeSeriesPersister {
  def write(company:Company, stockTimeSeries: TimeSeriesDaily)

  val measurement = "market_daily"
  def fieldKeyFor(symbol:String):String
}

sealed abstract class Kind(val key:String)
object Kind {
  case object Quote extends Kind("quote")
}

object TimeSeriesPersister {
  def fieldKeyFor(symbol:String, kind:Kind): String = {
    s"s_${symbol}_${kind.key}}"
  }
}
