package org.purevalue.mmon

object Masterdata {
  def sectors:Set[Sector] = BootstrapData.sectors
  def companies:List[Company] = BootstrapData.sp500Companies
}