package org.purevalue.mmon

import java.time.LocalDate

case class Quote(price: Float, volume: Long)
case class DayQuote(date: LocalDate, quote: Quote) extends Ordered[DayQuote] {
  override def compare(that: DayQuote): Int = this.date.compareTo(that.date)
}

case class TimeSeriesDaily(symbol: String, timeSeries: List[DayQuote])

case class Company(symbol: String, isin: String, wkn: String, name: String, sector: Sector) extends Comparable[Company] {
  override def compareTo(o: Company): Int = this.symbol.compareTo(o.symbol)
}

object Company {
  def apply(symbol: String, name: String, sector: Sector): Company = {
    Company(symbol, null, null, name, sector)
  }
}