package org.purevalue.mmon


object Masterdata {
  private var initialized:Boolean = false
  private var _companies:List[Company] = _
  private var _reportingGroups: List[ReportingGroup] = _

  def companies():List[Company] = { lazyInit; _companies }
  def reportingGroups():List[ReportingGroup] = { lazyInit; _reportingGroups }

  private def lazyInit:Unit = {
    if (!initialized) {
      init
      initialized=true
    }
  }
  private def init: Unit = {
    _companies = BootstrapData.sp500Companies
    _reportingGroups = companies
      .groupBy(_.sector)
      .map(m => ReportingGroup(m._1, m._2.map(_.symbol)))
      .toList
  }
}