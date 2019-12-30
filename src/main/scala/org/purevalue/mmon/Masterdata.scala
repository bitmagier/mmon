package org.purevalue.mmon

import org.purevalue.mmon.retrieve.masterdata.datahub.DatahubCompanyRetriever

object Masterdata {
  lazy val _companies: List[Company] = new DatahubCompanyRetriever()
    .retrieveCompanies()
    .filterNot(c => Config.dataCompanyBlacklist.contains(c.symbol))

  def sectors: Set[Sector] = Sector.all
  def companies: List[Company] = _companies
  def companyBySymbol(symbol: String): Company = companies.find(_.symbol == symbol).get
}