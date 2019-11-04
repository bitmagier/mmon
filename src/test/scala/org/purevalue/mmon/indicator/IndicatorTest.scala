package org.purevalue.mmon.indicator

import java.lang.Math.sqrt

import org.purevalue.mmon.{Quote, Sector}
import org.scalatest.FunSuite

class IndicatorTest extends FunSuite {
  test("SectorHarmonyIndicator") {
    val indicator = SectorHarmonyIndicator("foo", Sector.IT)
    val prevDay = Map(
      "a1" -> Quote(100.0f, 1L),
      "a2" -> Quote(150.0f, 1L))
    val currentDay = Map(
      "a1" -> Quote(105.0f, 1L),
      "a2" -> Quote(160.0f, 1L)
    )
    val indicatorValue = indicator.calc(prevDay, currentDay)

    val gain1 = (105.0-100.0)/100.0
    val gain2 = (160.0-150.0)/150.0
    val middle = (gain1+gain2)/2.0
    val expectedValue = (sqrt(middle-gain1) + sqrt(middle-gain2))/2

    assert(indicatorValue == expectedValue)
  }
}
