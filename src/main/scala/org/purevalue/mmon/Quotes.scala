package org.purevalue.mmon

import java.util.Locale.IsoCountryCode


case class Index(symbol:String, name:String, symbols:List[String])
case class Stock(symbol:String, name:String, industrialSector: String, headquarterLocation:String)

object Indicies {
  val Nasdaq100 = Index("NDX", "NASDAQ-100",
    List("ATVI", )
  )
}

object Masterdata {
  val Stocks :List[Stock] = List (
    Stock("ATVI", "Activision Blizzard", "Computer- und Videospiele", "Vereinigte Staaten"),
    Stock("ADBE",	"Adobe Inc.",	"Softwarehersteller", "Vereinigte Staaten"),
    Stock("ALXN", "Alexion Pharmaceuticals", "Orphan-Arzneimittel", 	"Vereinigte Staaten"),
  )
}


/*
Stock("ALGN	Align Technology	Medizintechnik	Vereinigte Staaten
Stock("GOOG, GOOGL	Alphabet Inc.	Internetdienstleistungen	Vereinigte Staaten
Stock("AMD	Advanced Micro Devices	Halbleiter	Vereinigte Staaten
Stock("AMZN	Amazon.com	Online-Versandhändler	Vereinigte Staaten
Stock("AAL	American Airlines Group	Fluggesellschaft	Vereinigte Staaten
Stock("AMGN	Amgen	Biotechnologie	Vereinigte Staaten
Stock("ADI	Analog Devices	Halbleiter	Vereinigte Staaten
Stock("AAPL	Apple	Hard- und Softwareentwicklung	Vereinigte Staaten
Stock("AMAT	Applied Materials	Anlagenbau	Vereinigte Staaten
Stock("ASML	ASML Holding	Halbleiter	Niederlande
Stock("ADSK	Autodesk	Softwarehersteller	Vereinigte Staaten
Stock("ADP	Automatic Data Processing	Personal-Dienstleister	Vereinigte Staaten
Stock("BIDU	Baidu	Suchmaschine	Volksrepublik China
Stock("BIIB	Biogen Idec	Biotechnologie	Vereinigte Staaten
Stock("BMRN	BioMarin Pharmaceutical		Vereinigte Staaten
Stock("BKNG	Booking Holdings	Online-Reiseportale	Vereinigte Staaten
Stock("AVGO	Broadcom Inc.	Halbleiter	Vereinigte Staaten
Stock("CDNS	Cadence Design Systems	Softwarehersteller	Vereinigte Staaten
Stock("CELG	Celgene	Pharmahersteller	Vereinigte Staaten
Stock("CERN	Cerner Corporation	Informationstechnik	Vereinigte Staaten
Stock("CHTR	Charter Communications	Kabelnetze	Vereinigte Staaten
Stock("CHKP	Check Point	Softwareunternehmen	Israel
Stock("CTAS	Cintas Corporation	Textilien	Vereinigte Staaten
Stock("CSCO	Cisco Systems	Telekommunikation	Vereinigte Staaten
Stock("CTXS	Citrix Systems	Software	Vereinigte Staaten
Stock("CTSH	Cognizant Technology Solutions	Software	Vereinigte Staaten
Stock("CMCSA	Comcast	Kabelnetzbetreiber	Vereinigte Staaten
Stock("COST	Costco Wholesale	Großhandel	Vereinigte Staaten
Stock("CSX	CSX Corporation	Eisenbahnunternehmen	Vereinigte Staaten
Stock("CTRP	Ctrip	Touristik	Volksrepublik China
Stock("DLTR	Dollar Tree	Billig-Gemischtwarenläden	Vereinigte Staaten
Stock("EBAY	eBay	Internetauktionshaus	Vereinigte Staaten
Stock("EA	Electronic Arts	Computer- und Videospiele	Vereinigte Staaten
Stock("EXPE	Expedia	Online-Touristik	Vereinigte Staaten
Stock("FB	Facebook	Soziale Medien	Vereinigte Staaten
Stock("FAST	Fastenal	Einzelhandel	Vereinigte Staaten
Stock("FISV	Fiserv	Zahlungsverkehr	Vereinigte Staaten
Stock("FOX, FOXA	Fox Corporation	Medien	Vereinigte Staaten
Stock("GILD	Gilead Sciences	Biotechnologie	Vereinigte Staaten
Stock("HAS	Hasbro	Spieleverlag	Vereinigte Staaten
Stock("HSIC	Henry Schein	Handel	Vereinigte Staaten
Stock("IDXX	Idexx Laboratories	Veterinär-Medizin	Vereinigte Staaten
Stock("ILMN	Illumina	Biotechnologie	Vereinigte Staaten
Stock("INCY	Incyte	Pharmaunternehmen	Vereinigte Staaten
Stock("INTC	Intel	Halbleiter	Vereinigte Staaten
INTU	Intuit	Standardanwendungssoftware	Vereinigte Staaten
ISRG	Intuitive Surgical	Medizintechnik	Vereinigte Staaten
JBHT	J. B. Hunt	Spedition	Vereinigte Staaten
JD	JD.com	E-Commerce	Volksrepublik China
KLAC	KLA-Tencor	Anlagenbau	Vereinigte Staaten
LRCX	Lam Research	Anlagenbau	Vereinigte Staaten
LBTYA, LBTYK	Liberty Global	Telekommunikation	Vereinigtes Königreich
LULU	Lululemon Athletica		Kanada
MAR	Marriott International	Hotel- und Gastgewerbe	Vereinigte Staaten
MXIM	Maxim Integrated	Halbleiter	Vereinigte Staaten
MELI	MercadoLibre	E-Commerce	Argentinien
MCHP	Microchip Technology	Halbleiter	Vereinigte Staaten
MU	Micron Technology	Halbleiter	Vereinigte Staaten
MSFT	Microsoft	Software- und Hardwarehersteller	Vereinigte Staaten
MDLZ	Mondelēz International	Nahrungsmittel	Vereinigte Staaten
MNST	Monster Beverage	Getränkeindustrie	Vereinigte Staaten
MYL	Mylan	Pharmazie	Niederlande
NTAP	NetApp	Speichertechnologie	Vereinigte Staaten
NTES	NetEase	Internetdienstleistungen	Volksrepublik China
NFLX	Netflix	Filmwirtschaft	Vereinigte Staaten
NVDA	Nvidia	Halbleiter	Vereinigte Staaten
NXPI	NXP Semiconductors	Halbleiter	Niederlande
ORLY	O’Reilly Auto Parts	Autoteile	Vereinigte Staaten
PCAR	Paccar	Nutzfahrzeugbau	Vereinigte Staaten
PAYX	Paychex	Finanzdienstleistungen	Vereinigte Staaten
PYPL	PayPal	Online-Bezahldienst	Vereinigte Staaten
PEP	PepsiCo	Getränke und Lebensmittel	Vereinigte Staaten
QCOM	Qualcomm	Telekommunikation	Vereinigte Staaten
REGN	Regeneron Pharmaceuticals	Pharmazie	Vereinigte Staaten
ROST	Ross Stores		Vereinigte Staaten
SIRI	Sirius XM Holdings	Satellitenrundfunk	Vereinigte Staaten
SWKS	Skyworks Solutions	Halbleiter	Vereinigte Staaten
SBUX	Starbucks	Kaffeeprodukte	Vereinigte Staaten
SYMC	Symantec	Softwareentwicklung	Vereinigte Staaten
SNPS	Synopsys	Softwareentwicklung	Vereinigte Staaten
TTWO	Take-Two Interactive	Computerspiele	Vereinigte Staaten
TSLA	Tesla, Inc.	Elektroautos	Vereinigte Staaten
TXN	Texas Instruments	Halbleiter	Vereinigte Staaten
KHC	The Kraft Heinz Company	Lebensmittel	Vereinigte Staaten
TMUS	T-Mobile US	Telekommunikation	Vereinigte Staaten
ULTA	Ulta Beauty	Kosmetik	Vereinigte Staaten
UAL	United Continental Holdings	Luftverkehr	Vereinigte Staaten
VRSN	Verisign	IT	Vereinigte Staaten
VRSK	Verisk Analytics	Datenanalyse	Vereinigte Staaten
VRTX	Vertex Pharmaceuticals		Vereinigte Staaten
WBA	Walgreens Boots Alliance	Drogerieprodukte Medizinprodukte	Vereinigte Staaten
WDC	Western Digital	Computerhardware	Vereinigte Staaten
WLTW	Willis Towers Watson	Unternehmensberatung	Irland
WDAY	Workday	Personal-Dienstleister	Vereinigte Staaten
WYNN	Wynn Resorts	Hotel- und Gastgewerbe	Vereinigte Staaten
XEL	Xcel Energy	Elekrtizität und Erdgas	Vereinigte Staaten
XLNX	Xilinx	Halbleiter	Vereinigte Staaten
*/

object Quotes {
  def refresh(): Unit = {

  }
}
