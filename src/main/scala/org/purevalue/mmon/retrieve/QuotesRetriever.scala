package org.purevalue.mmon.retrieve

import java.time.LocalDate


trait QuotesRetriever {
  def receiveFull(symbol:String): TimeSeriesDaily
}
