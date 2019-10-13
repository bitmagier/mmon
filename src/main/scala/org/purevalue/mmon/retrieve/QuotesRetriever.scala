package org.purevalue.mmon.retrieve

import org.purevalue.mmon.TimeSeriesDaily

trait QuotesRetriever {
  def receiveFull(symbol:String): TimeSeriesDaily
}


//class UsfundamentalsMasterdataRetriever {
//  val ApiToken = "ehQfD01rG7g133ENUPLL5A"
//
//}