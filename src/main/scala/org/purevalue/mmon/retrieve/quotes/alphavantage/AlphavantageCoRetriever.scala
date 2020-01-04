package org.purevalue.mmon.retrieve.quotes.alphavantage

import java.net.URL
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.{Duration, LocalDate, LocalDateTime}

import io.circe.{Decoder, HCursor, Json, parser}
import org.purevalue.mmon._
import org.purevalue.mmon.retrieve.{Retriever, UnknownSymbolException}
import org.slf4j.LoggerFactory

import scala.io.{BufferedSource, Source}

class AlphavantageCoRetriever(useTestResponse: Option[String] = None, preferLocalCachedData: Boolean = false) extends Retriever {
  private val log = LoggerFactory.getLogger(classOf[AlphavantageCoRetriever])
  private var lastApiCallTime: LocalDateTime = _

  private val AlphavantageHostname = "www.alphavantage.co"
  private val ApiKey = Config.alphavantageApiKey
  private val MaxApiCallRate = Config.alphavantageApiCallDelay


  private def getRawData(symbol: String): (String, Boolean) = {
    if (useTestResponse.isDefined) (useTestResponse.get, false)
    else if (preferLocalCachedData) {
      val cache = Cache.readFromLocalCache(symbol, Config.maxCacheAge)
      if (cache.isDefined) (cache.get, false)
      else (readFromApi(symbol), true)
    } else (readFromApi(symbol), true)
  }

  def decodeAndThrowError(rawData: String): Unit = {
    if (rawData.contains(""""Invalid API call. Please retry""")) {
      throw UnknownSymbolException(rawData)
    } else {
      throw new Exception(rawData)
    }
  }

  override def retrieveFull(symbol: String): TimeSeriesDaily = {
    val (rawData, isFreshData) = getRawData(symbol)
    if (rawData.contains(""""Error Message":""")) {
      decodeAndThrowError(rawData)
    }
    val result = parse(rawData)
    if (isFreshData) Cache.updateCache(symbol, rawData)
    result
  }

  private def apiEndpoint(apiKey: String, symbol: String): URL = new URL(s"https://$AlphavantageHostname/query?function=TIME_SERIES_DAILY&symbol=$symbol&outputsize=full&apikey=$apiKey")

  private def readFromApi(symbol: String): String = {
    val companyName = Masterdata.companyBySymbol(symbol).name
    log.info(s"Retrieving stock quotes for symbol '$symbol' (${companyName}) from $AlphavantageHostname")
    if (lastApiCallTime != null) {
      val toWaitMs = MaxApiCallRate.minus(Duration.between(lastApiCallTime, LocalDateTime.now())).toMillis
      if (toWaitMs > 0) {
        log.info(s"Waiting ${toWaitMs} ms before next API call to $AlphavantageHostname ...")
        Thread.sleep(toWaitMs)
      }
    }

    var reader: BufferedSource = null
    try {
      val url = apiEndpoint(ApiKey, symbol)
      log.debug(s"GET $url")
      reader = Source.fromURL(url)
      lastApiCallTime = LocalDateTime.now()
      reader.mkString
    } finally {
      if (reader != null) reader.close()
    }
  }

  def parse(rawData: String): TimeSeriesDaily = {
    case class ATimeSeries(quotes: List[DayQuote])
    case class AQuote(price: Float, volume: Long)

    try {
      val json: Json = parser.parse(rawData).toTry.get
      val metaData: Map[String, String] = json.hcursor.downField("Meta Data").as[Map[String, String]].toTry.get
      implicit val dayQuoteDecoder: Decoder[AQuote] =
        (hCursor: HCursor) => {
          for {
            close <- hCursor.get[Float]("4. close")
            volume <- hCursor.get[Long]("5. volume")
          } yield AQuote(close, volume)
        }

      val timeSeries: Map[String, AQuote] = json.hcursor.downField("Time Series (Daily)").as[Map[String, AQuote]].toTry.get

      TimeSeriesDaily(metaData("2. Symbol"),
        timeSeries.map(x =>
          DayQuote(
            LocalDate.parse(x._1, ISO_LOCAL_DATE),
            Quote(x._2.price, x._2.volume)
          )).toList
      )
    } catch {
      case e: Exception =>
        log.error(s"Could not parse quote raw data:\n$rawData", e)
        throw e
    }
  }
}