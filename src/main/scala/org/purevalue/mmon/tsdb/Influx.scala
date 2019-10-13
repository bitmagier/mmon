package org.purevalue.mmon.tsdb

object Influx {
  val Measurement = "market_quotes_daily"

  val TagSymbol: String = "symbol"
  val TagName: String = "name"
  val TagSector: String = "sector"

  val FieldPrice:String = "Price"
  val FieldVolume:String = "Volume"
}
