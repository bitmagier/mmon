package org.purevalue.mmon.retrieve.quotes.alphavantage

import java.time.LocalDate

import org.purevalue.mmon.retrieve.UnknownSymbolException
import org.purevalue.mmon.{DayQuote, Quote, TimeSeriesDaily}
import org.scalatest.FunSuite

class AlphavantageCoRetrieverTest extends FunSuite {

  test("Decoder working with sample data") {
    val SampleData =
    """{
  "Meta Data": {
    "1. Information": "Daily Prices (open, high, low, close) and Volumes",
    "2. Symbol": "MSFT",
    "3. Last Refreshed": "2019-09-13 13:14:34",
    "4. Output Size": "Full size",
    "5. Time Zone": "US/Eastern"
  },
  "Time Series (Daily)": {
    "2019-09-13": {
      "1. open": "137.7800",
      "2. high": "138.0600",
      "3. low": "136.5700",
      "4. close": "137.4300",
      "5. volume": "5927284"
    },
    "2019-09-12": {
      "1. open": "137.8500",
      "2. high": "138.4200",
      "3. low": "136.8700",
      "4. close": "137.5200",
      "5. volume": "24854822"
    }
  }
}"""

    val s:TimeSeriesDaily = new AlphavantageCoRetriever(Some(SampleData)).retrieveFull("_")
    println(s)
    assert(s.symbol == "MSFT")
    assert(s.timeSeries.size == 2)
    assert(s.timeSeries.head == DayQuote(LocalDate.of(2019,9,13), Quote(137.4300f, 5927284)))
  }


  test("Retriever can recognize error response") {
    val ErrorResponseInvalidApiCall =
      """{
    "Error Message": "Invalid API call. Please retry or visit the documentation (https://www.alphavantage.co/documentation/) for TIME_SERIES_DAILY."
}"""

    val r = new AlphavantageCoRetriever(Some(ErrorResponseInvalidApiCall))
    assertThrows[UnknownSymbolException](
      r.retrieveFull("_")
    )
  }
}
