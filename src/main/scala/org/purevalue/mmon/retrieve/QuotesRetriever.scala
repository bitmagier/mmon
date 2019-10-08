package org.purevalue.mmon.retrieve

import org.purevalue.mmon.TimeSeriesDaily

trait QuotesRetriever {
  def receiveFull(symbol:String): TimeSeriesDaily
}
