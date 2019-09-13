package org.purevalue.mmon.retrieve

import java.time.ZonedDateTime

import scala.concurrent.duration.Duration

case class Rate(time:ZonedDateTime, avgPrice:Float, volume:Long)
case class StockTimeSeries(symbol:String, timeInterval:Duration, timeSeries:List[Rate])

trait QuotesRetriever {
  def receiveFull(symbol:String): List[Rate]
}
