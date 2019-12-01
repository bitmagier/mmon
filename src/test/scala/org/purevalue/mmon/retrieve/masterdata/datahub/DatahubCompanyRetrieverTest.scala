package org.purevalue.mmon.retrieve.masterdata.datahub

import org.purevalue.mmon.{Company, Sector}
import org.scalatest.FunSuite

import scala.io.Source

class DatahubCompanyRetrieverTest extends FunSuite {
  test("Datahub.io company data parsing") {
    val companies:List[Company] = new DatahubCompanyRetriever().parse(
      Source.fromResource("constituents.json").mkString
    )
    assert(companies.size == 505)
    assert(companies.exists(p => p.symbol=="MMM" && p.sector == Sector.Industrials && p.name == "3M Company"))
  }
}
