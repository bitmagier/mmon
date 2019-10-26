package org.purevalue.mmon

import java.time.LocalDate

case class DayQuote(date:LocalDate, price:Float, volume:Long)
case class TimeSeriesDaily(symbol:String, timeSeries:List[DayQuote])

case class Company(symbol:String, isin:String, wkn:String, name:String, sector:Sector)
object Company {
  def apply(symbol:String, name:String, sector:Sector):Company = {
    Company(symbol, null, null, name, sector)
  }
}
