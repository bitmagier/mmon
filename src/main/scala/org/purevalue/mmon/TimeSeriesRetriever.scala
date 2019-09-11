package org.purevalue.mmon

import java.time.ZonedDateTime

import scala.concurrent.duration.Duration

case class Rate(time:ZonedDateTime, avgPrice:Float, volume:Long)
case class StockTimeSeries(symbol:String, timeInterval:Duration, timeSeries:List[Rate])

trait TimeSeriesRetriever {
  def spportedDurations(): List[Duration]
  def receive(symbol:String, from:ZonedDateTime): List[Rate]
}
