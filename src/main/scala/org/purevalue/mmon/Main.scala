package org.purevalue.mmon

import org.slf4j.LoggerFactory

object Main {
  private val log = LoggerFactory.getLogger("main")

  def main(args: Array[String]): Unit = {

    Config.validate()

    if (args.isEmpty)
      log.warn("Please provide action via commandline parameter.\n"
        + "l = InitialLoad\n"
        + "c = Continue mode using existing database\n"
        + "i = Apply indicators only - requires previously loaded database with quotes"
      )
    else {
      args.head match {
        case "l" => Load.initialLoad()
        case "c" => Load.initialLoad(dropDatabase = false)
        case "i" => Load.applyIndicators()
        case _ => log.error(s"Unsupported action ${args.head}")
      }
    }
  }
}

// TODO real continue mode rememebering already imported company quotes of the day