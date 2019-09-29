package org.purevalue.mmon.retrieve

import org.purevalue.mmon.Company


object MasterdataRetriever {
//  def getCompanyBySymbol(symbol: String): Company = ???
  def indexMember(indexSymbol:String): List[Company] = ???

}

trait RawMasterdataRetriever {
  private val usfundamentalsMasterdataRetriever = new UsfundamentalsMasterdataRetriever()
  def indexMember(indexSymbol:String): List[]
}
