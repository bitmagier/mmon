package org.purevalue.mmon.retrieve.masterdata.datahub

import io.circe.{Decoder, HCursor, Json, parser}
import org.purevalue.mmon.retrieve.CompanyRetriever
import org.purevalue.mmon.{Company, Sector}
import org.slf4j.LoggerFactory

import scala.io.Source

class DatahubCompanyRetriever extends CompanyRetriever {
  private val log = LoggerFactory.getLogger(classOf[DatahubCompanyRetriever])
  private val Url = "https://datahub.io/core/s-and-p-500-companies/r/constituents.json"

  private def retrieveJson():String = {
    val reader = Source.fromURL(Url)
    reader.mkString
  }

  /*
  [
    {
      "Name": "3M Company",
      "Sector": "Industrials",
      "Symbol": "MMM"
    },
      ...
  ]
  */
  private[datahub] def parse(rawData: String): List[Company] = {
    implicit val companyDecoder: Decoder[Company] =
      (hCursor: HCursor) => {
        for {
          name <- hCursor.get[String]("Name")
          sector <- hCursor.get[String]("Sector")
          symbol <- hCursor.get[String]("Symbol")
        } yield Company(symbol, name, Sector.parse(sector))
      }
    val json: Json = parser.parse(rawData).toTry.get
    json.hcursor.as[List[Company]].toTry.get
  }

  override def retrieveCompanies(): List[Company] = {
    log.info("Getting company masterdata from datahub.io")
    parse(retrieveJson())
  }
}
