package org.purevalue.mmon.indicator

import org.purevalue.mmon.{Quote, Sector}
import org.scalactic.{Equality, TolerantNumerics}
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

    val gain1:Double = (105.0d - 100.0)/100.0
    val gain2:Double = (160.0d - 150.0)/150.0
    val middle:Double = (gain1+gain2)/2.0d
    val expectedValue:Float = ((Math.pow(middle-gain1, 2.0) + Math.pow(middle-gain2, 2.0))/2).toFloat

    val tolerance = 1.0e-5f
    implicit val floatEq:Equality[Float] = TolerantNumerics.tolerantFloatEquality(tolerance);
    assert(indicatorValue === expectedValue)
  }
}
