package org.purevalue.mmon

import java.time.Duration

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Config {
  private val log = LoggerFactory.getLogger("org.purevalue.mmon.Config")
  def validate(): Unit = {
    for (f <- dataBusinessSectorFilter) {
      if (!Masterdata.sectors.map(_.name).contains(f)) {
        log.warn(s"Config contains invalid value '$f' under 'data.business-sector-filter'")
      }
    }
  }

  private val conf = ConfigFactory.load()

  def alphavantageApiKey: String = conf.getString("retrieve.alphavantage.api-key")
  def alphavantageApiCallDelay: Duration = conf.getDuration("retrieve.alphavantage.api-call-delay")
  def alphavantageCacheDir:String = conf.getString("retrieve.alphavantage.cache-dir")
  def maxMissingCompanies:Int = conf.getInt("retrieve.max-missing-companies")

  def influxdbHostname: String = conf.getString("influxdb.hostname")
  def influxdbDatabase: String = conf.getString("influxdb.database")
  def influxdbMeasurementQuote: String = conf.getString("influxdb.measurement.quote")
  def influxdbMeasurementIndicator: String = conf.getString("influxdb.measurement.indicator")
  def influxAsyncReadTimeout: Duration = conf.getDuration("influxdb.timeout.bulk-read")
  def influxAsyncWriteTimeout: Duration = conf.getDuration("influxdb.timeout.bulk-write")

  def dataBusinessSectorFilter:Set[String] = conf.getStringList("data.business-sector-filter").asScala.toSet

}
