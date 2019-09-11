package org.purevalue.mmon

trait TimeSeriesPersister {
  def persist(stockTimeSeries: StockTimeSeries)
}
