package org.purevalue.mmon.tsdb

object Influx {
  val influxHostName = "localhost"
  val influxDbName = "mmon"

  val Measurement = "MarketQuotesDaily"

  val TagSymbol: String = "symbol"
  val TagName: String = "name"
  val TagSector: String = "sector"

  val FieldPrice:String = "price"
  val FieldVolume:String = "volume"
}
