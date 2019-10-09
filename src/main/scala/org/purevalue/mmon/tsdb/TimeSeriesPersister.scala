package org.purevalue.mmon.tsdb

import org.purevalue.mmon.{Company, TimeSeriesDaily}

trait TimeSeriesPersister {
  val MarketQuotesDaily = "market_quotes_daily"
  def write(company:Company, stockTimeSeries: TimeSeriesDaily)
}
