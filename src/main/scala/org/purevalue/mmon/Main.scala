package org.purevalue.mmon

import org.slf4j.LoggerFactory

object Main {
  private val log = LoggerFactory.getLogger("main")

  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      log.warn("Please provide action via commandline parameter.\ni = InitialLoad (pure)\nc = InitialLoad using local cached stock data")
    else {
      args.head match {
        case "i" => InitialLoad.initialLoad()
        case "c" => InitialLoad.initialLoad(true)
        case _ => log.error(s"Unsupported action ${args.head}")
      }
    }
  }
}


// TODO make importCompanyQuotes (especially InfluxDb.write) async
// TODO (for production) enable HTTPS with InfluxDB: https://docs.influxdata.com/influxdb/v1.7/administration/https_setup/
