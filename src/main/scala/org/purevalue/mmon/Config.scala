package org.purevalue.mmon

import java.time.Duration

import com.typesafe.config.ConfigFactory

object Config {
  private val conf = ConfigFactory.load()

  def alphavantageApiKey: String = conf.getString("retrieve.alphavantage.api-key")
  def alphavantageApiCallDelay: Duration = conf.getDuration("retrieve.alphavantage.api-call-delay")
  def influxdbHostname: String = conf.getString("influxdb.hostname")
  def influxdbDatabase: String = conf.getString("influxdb.database")
  def influxdbMeasurement: String = conf.getString("influxdb.measurement")
  def influxAsyncWriteTimeout: Duration = conf.getDuration("influxdb.async-write-timeout")
}
