package org.purevalue.mmon.retrieve
import scala.io.Source

class AplhavantageCoRetriever extends QuotesRetriever {
  val ApiKey = "TYEY6NJ2ZS8UXGB8"


  override def receiveFull(symbol: String): List[Rate] = {
    val ApiEndpoint = s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=${symbol}&outputsize=full&apikey=${ApiKey}"
    val output = Source.fromURL(ApiEndpoint)
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    // val parsedJson = mapper.readValue[Map[String, Object]](json.reader())
  }
}

/*
 * {
 * "Meta Data": {
 * "1. Information": "Daily Prices (open, high, low, close) and Volumes",
 * "2. Symbol": "MSFT",
 * "3. Last Refreshed": "2019-09-13 13:14:34",
 * "4. Output Size": "Full size",
 * "5. Time Zone": "US/Eastern"
 * },
 * "Time Series (Daily)": {
 * "2019-09-13": {
 * "1. open": "137.7800",
 * "2. high": "138.0600",
 * "3. low": "136.5700",
 * "4. close": "137.4300",
 * "5. volume": "5927284"
 * },
 * "2019-09-12": {
 * "1. open": "137.8500",
 * "2. high": "138.4200",
 * "3. low": "136.8700",
 * "4. close": "137.5200",
 * "5. volume": "24854822"
 * },
 */