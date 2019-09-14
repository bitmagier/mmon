package org.purevalue.mmon.retrieve

import java.time.LocalDate

case class DayRate(time:LocalDate, price:Float, volume:Long)
case class StockTimeSeriesDaily(symbol:String, timeSeries:List[DayRate])

trait QuotesRetriever {
  def receiveFull(symbol:String): StockTimeSeriesDaily
}
