package org.purevalue.mmon

object Masterdata {
  private var initialized:Boolean = false
  private var _companies:List[Company] = _

  def companies:List[Company] = { lazyInit(); _companies }

  private def lazyInit():Unit = {
    if (!initialized) {
      init()
      initialized=true
    }
  }

  private def init(): Unit = {
    _companies = BootstrapData.sp500Companies
  }
}