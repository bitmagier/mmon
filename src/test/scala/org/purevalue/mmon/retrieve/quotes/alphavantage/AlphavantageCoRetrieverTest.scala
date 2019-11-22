package org.purevalue.mmon.retrieve.quotes.alphavantage

import java.time.LocalDate

import org.purevalue.mmon.{DayQuote, Quote, TimeSeriesDaily}
import org.scalatest.FunSuite

class AlphavantageCoRetrieverTest extends FunSuite {
  test("Decoder working with sample data") {
    val s:TimeSeriesDaily = new AlphavantageCoRetriever(true).retrieveFull("_")
    println(s)
    assert(s.symbol == "MSFT")
    assert(s.timeSeries.size == 2)
    assert(s.timeSeries.head == DayQuote(LocalDate.of(2019,9,13), Quote(137.4300f, 5927284)))
  }
}
