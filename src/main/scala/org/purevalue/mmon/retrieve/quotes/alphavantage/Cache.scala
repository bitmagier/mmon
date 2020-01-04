package org.purevalue.mmon.retrieve.quotes.alphavantage

import java.io.{BufferedWriter, File, FileWriter}
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

import org.purevalue.mmon.Config
import org.slf4j.LoggerFactory

import scala.io.Source

private[alphavantage] object Cache {
  private val log = LoggerFactory.getLogger("org.purevalue.mmon.retrieve.quotes.alphavantage.Cache")
  private val localCacheDir: File = new File(Config.alphavantageCacheDir)

  private def clearCache(symbol: String): Unit = {
    localCacheDir
      .listFiles((_, file) => file.startsWith(symbol) && file.endsWith(".rawdata"))
      .foreach {
        _.delete()
      }
  }

  private def cacheFileName(symbol:String, age:Int): File = {
    val day: String = ISO_LOCAL_DATE.format(LocalDate.now().minusDays(age))
    new File(localCacheDir, s"$symbol-$day.rawdata")
  }

  def updateCache(symbol: String, rawData: String): Unit = {
    localCacheDir.mkdirs()
    clearCache(symbol)

    val w = new BufferedWriter(new FileWriter(cacheFileName(symbol, 0)))
    w.write(rawData)
    w.close()
  }

  def readFromLocalCache(symbol:String, maxCacheAge:Int):Option[String] = {
    var age:Int=0

    do {
      val f = cacheFileName(symbol, age)
      if (f.exists()) {
        log.info(s"Reading rawdata (age: $age days) for symbol $symbol from local cache")
        val s = Source.fromFile(f)
        val result = s.mkString
        s.close()
        return Option(result)
      }
      age += 1
    } while (age <= maxCacheAge)

    log.info(s"No up-to-date local cache file present for '$symbol'")
    Option.empty
  }
}
