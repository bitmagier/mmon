package org.purevalue.mmon

import org.purevalue.mmon.retrieve.masterdata.datahub.DatahubCompanyRetriever

object Masterdata {
  def sectors:Set[Sector] = Sector.all
  def companies:List[Company] = new DatahubCompanyRetriever().retrieveCompanies()
}