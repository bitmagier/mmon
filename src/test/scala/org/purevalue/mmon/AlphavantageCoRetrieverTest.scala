package org.purevalue.mmon

import java.time.LocalDate

import org.purevalue.mmon.retrieve.{AplhavantageCoRetriever, DayRate, StockTimeSeriesDaily}
import org.scalatest.FunSuite

class AlphavantageCoRetrieverTest extends FunSuite {
  test("Decoder working with sample data") {
    val s:StockTimeSeriesDaily = new AplhavantageCoRetriever(true).receiveFull("_")
    assert(s.symbol == "MSFT")
    assert(s.timeSeries.size == 2)
    assert(s.timeSeries.head == DayRate(LocalDate.of(2019,9,13), 137.4300f, 5927284))
  }
}
