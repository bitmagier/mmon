package org.purevalue.mmon.retrieve

import org.purevalue.mmon.{Company, TimeSeriesDaily}

trait Retriever {
  def retrieveFull(symbol:String): TimeSeriesDaily
}

trait CompanyRetriever {
  def retrieveCompanies(): List[Company]
}

//class UsfundamentalsMasterdataRetriever {
//  val ApiToken = "ehQfD01rG7g133ENUPLL5A"
//
//}