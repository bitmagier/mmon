package org.purevalue.mmon.tsdb

import org.purevalue.mmon.{Company, TimeSeriesDaily}

trait TimeSeriesPersister {
  def write(company:Company, stockTimeSeries: TimeSeriesDaily)

  def fieldKeyFor(symbol:String):String
}

object TimeSeriesPersister {
  def fieldKeyFor(symbol: String): String = {
    s"s_${symbol}_quote"
  }
}
