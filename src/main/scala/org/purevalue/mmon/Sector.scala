package org.purevalue.mmon

sealed abstract class Sector(val name:String, val name2:String=null)
object Sector {
  case object Industrials extends Sector("Industrials")
  case object Healthcare extends Sector("Healthcare", "Health Care")
  case object IT extends Sector("IT", "Information Technology")
  case object ConsumerDiscretionary extends Sector("ConsumerDiscretionary", "Consumer Discretionary")
  case object Financials extends Sector("Financials")
  case object Materials extends Sector("Materials")
  case object RealEstate extends Sector("RealEstate", "Real Estate")
  case object ConsumerStaples extends Sector("ConsumerStaples", "Consumer Staples")
  case object Utilities extends Sector("Utilities")
  case object Energy extends Sector("Energy")
  case object TelecommunicationServices extends Sector ("TelecommunicationServices", "Telecommunication Services")

  def all: Set[Sector] = Set(Industrials, Healthcare, IT, ConsumerDiscretionary, Financials, Materials, RealEstate, ConsumerStaples, Utilities, Energy, TelecommunicationServices)
  def parse(name:String): Sector = {
    val s = all.find(x => x.name == name || x.name2 == name)
    if (s.isEmpty) throw new NoSuchElementException(s"Sector with name $name does not exist")
    s.get
  }
}

