package org.purevalue.mmon

import java.time.LocalDate

case class DayQuote(time:LocalDate, price:Float, volume:Long)
case class TimeSeriesDaily(symbol:String, timeSeries:List[DayQuote])

case class Company(symbol:String, isin:String, wkn:String, name:String, sector:String)
object Company {
  def apply(symbol:String, name:String, sector:String):Company = {
    Company(symbol, null, null, name, sector)
  }
}
case class ReportingGroup(name:String, members:List[Company])
