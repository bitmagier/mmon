package org.purevalue.mmon

import org.slf4j.LoggerFactory

object Main {
  private val log = LoggerFactory.getLogger("main")

  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      log.warn("Please provide action via commandline parameter.\n" +
        "l = InitialLoad (pure)\n" +
        "c = InitialLoad using local cached stock data\n" +
        "i = Apply indicators only - requires previously loaded data")
    else {
      args.head match {
        case "l" => InitialLoad.initialLoad()
        case "c" => InitialLoad.initialLoad(true)
        case "i" => InitialLoad.applyIndicators()
        case _ => log.error(s"Unsupported action ${args.head}")
      }
    }
  }
}


// TODO make importCompanyQuotes (especially InfluxDb.write) async
// TODO (for production) enable HTTPS with InfluxDB: https://docs.influxdata.com/influxdb/v1.7/administration/https_setup/
