package org.purevalue.mmon.tsdb

import org.purevalue.mmon.Config

object Influx {
  val influxHostName: String = Config.influxdbHostname
  val influxDbName: String = Config.influxdbDatabase

  val MeasurementQuote: String = Config.influxdbMeasurementQuote
  val MeasurementIndicator: String = Config.influxdbMeasurementIndicator

  val TagSymbol: String = "symbol"
  val TagName: String = "name"
  val TagSector: String = "sector"

  val FieldPrice: String = "price"
  val FieldVolume: String = "volume"
}
