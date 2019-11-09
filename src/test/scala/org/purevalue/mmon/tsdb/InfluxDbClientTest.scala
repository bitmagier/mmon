package org.purevalue.mmon.tsdb

import java.time.LocalDate

import org.purevalue.mmon.retrieve.AlphavantageCoRetriever
import org.purevalue.mmon._
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.io.Source

/** This test requires a running influxdb  - {@see infrastructure/dev/run-docker-based-dev-infrastructure.sh} */
class InfluxDbClientTest extends FunSuite with BeforeAndAfter {
  var db:InfluxDbClient = _

  before {
    db = new InfluxDbClient(Config.influxdbHostname, "UnitTestDatabase")
  }
  after {
    db.dropDatabase()
  }

  test("we can read the quotes, what we have written") {
    val company1 = BootstrapData.sp500Companies.find(c => c.symbol == "AAL").get
    val ts1 = new AlphavantageCoRetriever().parse(
      Source.fromResource("mmon-cache/AAL-2019-10-20.rawdata").mkString)
    db.writeQuotes(company1, ts1)

    val company2 = BootstrapData.sp500Companies.find(c => c.symbol == "GOOGL").get
    val ts2 = new AlphavantageCoRetriever().parse(
      Source.fromResource("mmon-cache/GOOGL-2019-10-20.rawdata").mkString)
    db.writeQuotes(company2, ts2)


    val q: List[TimeSeriesDaily] = db.readQuotes(LocalDate.ofYearDay(2000, 1), LocalDate.ofYearDay(2020,1)).toList

    assert(q.groupBy(_.symbol).keySet == Set("AAL", "GOOGL"))

    // check if we have same dates as we stored
    assert(
      q.filter(_.symbol == "AAL")
        .flatMap(_.timeSeries.map(_.date))
        .toSet
        ==
        ts1.timeSeries.map(_.date).toSet
    )
    assert(
      q.filter(_.symbol == "GOOGL")
        .flatMap(_.timeSeries.map(_.date))
        .toSet
        ==
        ts2.timeSeries.map(_.date).toSet
    )

    //    "2006-01-27": {
    //      "1. open": "435.0162",
    //      "2. high": "438.2200",
    //      "3. low": "433.4900",
    //      "4. close": "433.4900",
    //      "5. volume": "16887400"
    //    },
    assert(
      q.filter(_.symbol == "GOOGL")
        .flatMap(_.timeSeries)
        .contains(DayQuote(LocalDate.of(2006, 1, 27), Quote(433.49f, 16887400L)))
    )
  }
}
