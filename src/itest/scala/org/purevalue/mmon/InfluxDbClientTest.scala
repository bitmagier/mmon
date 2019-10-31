package org.purevalue.mmon

import org.purevalue.mmon.retrieve.AlphavantageCoRetriever
import org.purevalue.mmon.tsdb.InfluxDbClient
import org.scalatest.FunSuite

import scala.io.Source

class InfluxDbClientTest extends FunSuite {
  test("we read what we write") {
    val db = new InfluxDbClient(Config.influxdbHostname, "UnitTestDatabase")

    val company1 = BootstrapData.sp500Companies.find(c => c.symbol == "AAL").get
    val ts1 = new AlphavantageCoRetriever().parse(
      Source.fromResource("mmon-cache/AAL-2019-10-20.rawdata").mkString)
    db.write(company1, ts1)

    val company2 = BootstrapData.sp500Companies.find(c => c.symbol == "GOOGL").get
    val ts2 = new AlphavantageCoRetriever().parse(
      Source.fromResource("mmon-cache/GOOGL-2019-10-20.rawdata").mkString)
    db.write(company2, ts2)


    val q:List[TimeSeriesDaily]  = db.readQuotes().toList

    assert(q.groupBy(_.symbol).keySet == Set("AAL", "GOOGL"))
    // TODO
  }
}
