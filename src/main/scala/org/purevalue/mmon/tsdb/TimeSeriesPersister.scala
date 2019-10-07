package org.purevalue.mmon.tsdb

import org.purevalue.mmon.TimeSeriesDaily

trait TimeSeriesPersister {
  def write(stockTimeSeries: TimeSeriesDaily)
}
