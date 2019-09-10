package org.purevalue.mmon

import java.time.ZonedDateTime

import scala.concurrent.duration.Duration

case class TimeSeries(time:ZonedDateTime, avgPrice:Float, volume:Long)
case class StockTimeSeries(symbol:String, timeInterval:Duration, timeSeries:List[TimeSeries])

trait StockTimeSeriesRetriever {
  def spportedDurations(): Seq[Duration]
  def receiveIntraday(symbol:String, fromtimeInterval:Duration)
}
