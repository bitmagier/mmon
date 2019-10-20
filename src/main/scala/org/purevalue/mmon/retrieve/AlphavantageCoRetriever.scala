package org.purevalue.mmon.retrieve

import java.io.{BufferedWriter, File, FileWriter}
import java.net.URL
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.{Duration, LocalDate, LocalDateTime}

import io.circe.{Decoder, HCursor, Json, parser}
import org.purevalue.mmon.{Config, DayQuote, TimeSeriesDaily}
import org.slf4j.LoggerFactory

import scala.io.{BufferedSource, Source}


class AlphavantageCoRetriever(useSampleData: Boolean = false, preferLocalCachedData: Boolean = false) extends QuotesRetriever {
  private val log = LoggerFactory.getLogger(classOf[AlphavantageCoRetriever])
  private val localCacheDir = new File(Option(System.getProperty("java.io.tmpdir")).getOrElse("/tmp") + "/mmon-cache")
  private var lastApiCallTime: LocalDateTime = _

  private val AlphavantageHostname = "www.alphavantage.co"
  private val ApiKey = Config.alphavantageApiKey
  private val MaxApiCallRate = Config.alphavantageApiCallDelay


  private val SampleData =
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

  override def receiveFull(symbol: String): TimeSeriesDaily = {
    val rawData: String =
      if (useSampleData) SampleData
      else if (preferLocalCachedData) readFromLocalCache(symbol).getOrElse(readFromApi(symbol))
      else readFromApi(symbol)
    parse(rawData)
  }

  private def apiEndpoint(apiKey: String, symbol: String): URL = new URL(s"https://$AlphavantageHostname/query?function=TIME_SERIES_DAILY&symbol=$symbol&outputsize=full&apikey=$apiKey")

  private def readFromApi(symbol: String): String = {
    log.info(s"Retrieving stock quotes for symbol '$symbol' from $AlphavantageHostname")
    if (lastApiCallTime != null) {
      val toWait = MaxApiCallRate.minus(Duration.between(lastApiCallTime, LocalDateTime.now()))
      if (toWait.toMillis > 0)
        log.info(s"Waiting ${toWait.getSeconds} seconds before next API call to $AlphavantageHostname ...")
        Thread.sleep(toWait.toMillis)
    }

    var reader: BufferedSource = null
    try {
      reader = Source.fromURL(apiEndpoint(ApiKey, symbol))
      lastApiCallTime = LocalDateTime.now()
      val result = reader.mkString
      updateCache(symbol, result)
      result
    } finally {
      if (reader != null) reader.close()
    }
  }

  private def parse(rawData: String): TimeSeriesDaily = {
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
            x._2.price,
            x._2.volume
          )).toList
      )
    } catch {
      case e: Exception =>
        log.error(s"Could not parse quote raw data:\n$rawData", e)
        throw e
    }
  }

  private def updateCache(symbol: String, rawData: String):Unit = {
    localCacheDir.mkdirs()
    clearCache(symbol)

    val w = new BufferedWriter(new FileWriter(cacheFile(symbol)))
    w.write(rawData)
    w.close()
  }

  private def clearCache(symbol: String):Unit = {
    localCacheDir
      .listFiles((_, file) => file.startsWith(symbol) && file.endsWith(".rawdata"))
      .foreach {
        _.delete()
      }
  }

  def readFromLocalCache(symbol: String): Option[String] = {
    val f = cacheFile(symbol)
    if (f.exists()) {
      log.info(s"reading rawdata for symbol $symbol from local cache")
      val s = Source.fromFile(f)
      val result = s.mkString
      s.close()
      Option(result)
    } else {
      log.warn(s"no up-to-date local cache file present for '$symbol'")
      Option.empty
    }
  }

  private def cacheFile(symbol: String): File = {
    val day: String = ISO_LOCAL_DATE.format(LocalDate.now())
    new File(localCacheDir, s"$symbol-$day.rawdata")
  }
}