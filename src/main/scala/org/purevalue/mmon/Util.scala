package org.purevalue.mmon

import java.time.LocalDate

object Util {
  def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)
}
