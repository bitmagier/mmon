package org.purevalue.mmon

object Mmon {
  def main(args: Array[String]): Unit = {
    println("starting")
    Quotes().refresh()
    new Indicators().calc()
    println("done")
  }
}
