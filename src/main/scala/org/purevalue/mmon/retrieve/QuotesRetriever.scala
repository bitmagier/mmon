package org.purevalue.mmon.retrieve

import java.time.LocalDate

case class DayQuote(time:LocalDate, price:Float, volume:Long)
case class StockTimeSeriesDaily(symbol:String, timeSeries:List[DayQuote])

trait QuotesRetriever {
  def receiveFull(symbol:String): StockTimeSeriesDaily
}
