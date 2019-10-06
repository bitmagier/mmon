package org.purevalue.mmon

import org.purevalue.mmon.rdb.indicator.Indices
import org.purevalue.mmon.tsdb.InfluxdbPersister

object MmonInitialLoad {

  def importRawData() = {
    createTables()
    importCompanyQuotes()
  }

  def calculate() = {
    val tsdb = new InfluxdbPersister()
    Indices.foreach(
        tsdb.write(_.calculate())
    )
  }

  def exportTimeSeriesData() = {
    Indices.foreach()
  }

  def main(args: Array[String]): Unit = {
    println("starting")
    importRawData()
    calculate()
    exportTimeSeriesData()
    println("done")
  }
}
