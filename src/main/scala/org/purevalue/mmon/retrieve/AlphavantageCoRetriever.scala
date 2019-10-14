package org.purevalue.mmon.retrieve
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.circe.{Decoder, HCursor, Json, parser}
import org.purevalue.mmon.{DayQuote, TimeSeriesDaily}
import org.slf4j.LoggerFactory

import scala.io.{BufferedSource, Source}



class AlphavantageCoRetriever(useSampleData:Boolean = false) extends QuotesRetriever {
  private val log = LoggerFactory.getLogger(classOf[AlphavantageCoRetriever])

  val SampleData ="""{
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

  private val AlphavantageHostname = "www.alphavantage.co"
  private val ApiKey = "TYEY6NJ2ZS8UXGB8"

  private def apiEndpoint(apiKey:String, symbol:String):URL = new URL(s"https://$AlphavantageHostname/query?function=TIME_SERIES_DAILY&symbol=$symbol&outputsize=full&apikey=$apiKey")

  private def readFromApi(symbol:String): String = {
    var reader : BufferedSource = null
    try {
      reader = Source.fromURL(apiEndpoint(ApiKey,symbol))
      reader.mkString
    } finally {
      if (reader != null) reader.close()
    }
  }

  override def receiveFull(symbol: String): TimeSeriesDaily = {
    case class ATimeSeries(quotes:List[DayQuote])
    case class AQuote(price:Float, volume:Long)

    log.info(s"Retrieving full stock quotes for symbol '$symbol' from $AlphavantageHostname")
    val rawData:String = if (useSampleData) SampleData else readFromApi(symbol)

    val json: Json = parser.parse(rawData).toTry.get
    val metaData:Map[String,String] = json.hcursor.downField("Meta Data").as[Map[String,String]].toTry.get
    implicit val dayQuoteDecoder: Decoder[AQuote] =
      (hCursor: HCursor) => {
        for {
          close <- hCursor.get[Float]("4. close")
          volume <- hCursor.get[Long]("5. volume")
        } yield AQuote(close, volume)
      }

    val timeSeries:Map[String, AQuote] = json.hcursor.downField("Time Series (Daily)").as[Map[String, AQuote]].toTry.get

    TimeSeriesDaily(metaData("2. Symbol"),
      timeSeries.map(x =>
        DayQuote(
          LocalDate.parse(x._1, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
          x._2.price,
          x._2.volume
        )).toList
    )
  }
}