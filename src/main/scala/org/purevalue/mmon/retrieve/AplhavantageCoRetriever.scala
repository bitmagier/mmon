package org.purevalue.mmon.retrieve
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.circe.{Decoder, HCursor, Json, parser}

import scala.io.Source



class AplhavantageCoRetriever(useSampleData:Boolean = false) extends QuotesRetriever {

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


  def readFromApi(symbol:String): String = {
    val ApiKey = "TYEY6NJ2ZS8UXGB8"
    val ApiEndpoint = s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=$symbol&outputsize=full&apikey=$ApiKey"
    val reader = Source.fromURL(ApiEndpoint)
    val result = reader.mkString
    reader.close()
    result
  }

  override def receiveFull(symbol: String): StockTimeSeriesDaily = {
    case class TimeSeries(quotes:List[DayQuote])
    case class DayQuote(price:Float, volume:Long)


    val rawData:String = if (useSampleData) SampleData else readFromApi(symbol)

    val json: Json = parser.parse(rawData).toTry.get
    val metaData:Map[String,String] = json.hcursor.downField("Meta Data").as[Map[String,String]].toTry.get
    implicit val dayQuoteDecoder: Decoder[DayQuote] =
      (hCursor: HCursor) => {
        for {
          close <- hCursor.get[Float]("4. close")
          volume <- hCursor.get[Long]("5. volume")
        } yield DayQuote(close, volume)
      }

    val timeSeries:Map[String, DayQuote] = json.hcursor.downField("Time Series (Daily)").as[Map[String, DayQuote]].toTry.get

    StockTimeSeriesDaily(metaData("2. Symbol"),
      timeSeries.map(x =>
        DayRate(
          LocalDate.parse(x._1, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
          x._2.price,
          x._2.volume
        )).toList
    )
  }
}