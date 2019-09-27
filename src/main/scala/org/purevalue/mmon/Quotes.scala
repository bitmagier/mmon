package org.purevalue.mmon

import java.util.Locale.IsoCountryCode


case class Index(symbol:String, name:String, members:List[Symbol])
case class Stock(symbol:String, name:String, industrialSector: String, headquarterLocation:IsoCountryCode)

object Indicies {
  val Nasdaq100 = Index("NDX", "NASDAQ-100",
    List(
      Stock("ATVI", "Activision Blizzard", "Computer- und Videospiele", IsoCountryCode.)
    ))
}

/*
ADBE	Adobe Inc.	Softwarehersteller	Vereinigte Staaten
ALXN	Alexion Pharmaceuticals	Orphan-Arzneimittel	Vereinigte Staaten
ALGN	Align Technology	Medizintechnik	Vereinigte Staaten
GOOG, GOOGL	Alphabet Inc.	Internetdienstleistungen	Vereinigte Staaten
AMD	Advanced Micro Devices	Halbleiter	Vereinigte Staaten
AMZN	Amazon.com	Online-Versandhändler	Vereinigte Staaten
AAL	American Airlines Group	Fluggesellschaft	Vereinigte Staaten
AMGN	Amgen	Biotechnologie	Vereinigte Staaten
ADI	Analog Devices	Halbleiter	Vereinigte Staaten
AAPL	Apple	Hard- und Softwareentwicklung	Vereinigte Staaten
AMAT	Applied Materials	Anlagenbau	Vereinigte Staaten
ASML	ASML Holding	Halbleiter	Niederlande
ADSK	Autodesk	Softwarehersteller	Vereinigte Staaten
ADP	Automatic Data Processing	Personal-Dienstleister	Vereinigte Staaten
BIDU	Baidu	Suchmaschine	Volksrepublik China
BIIB	Biogen Idec	Biotechnologie	Vereinigte Staaten
BMRN	BioMarin Pharmaceutical		Vereinigte Staaten
BKNG	Booking Holdings	Online-Reiseportale	Vereinigte Staaten
AVGO	Broadcom Inc.	Halbleiter	Vereinigte Staaten
CDNS	Cadence Design Systems	Softwarehersteller	Vereinigte Staaten
CELG	Celgene	Pharmahersteller	Vereinigte Staaten
CERN	Cerner Corporation	Informationstechnik	Vereinigte Staaten
CHTR	Charter Communications	Kabelnetze	Vereinigte Staaten
CHKP	Check Point	Softwareunternehmen	Israel
CTAS	Cintas Corporation	Textilien	Vereinigte Staaten
CSCO	Cisco Systems	Telekommunikation	Vereinigte Staaten
CTXS	Citrix Systems	Software	Vereinigte Staaten
CTSH	Cognizant Technology Solutions	Software	Vereinigte Staaten
CMCSA	Comcast	Kabelnetzbetreiber	Vereinigte Staaten
COST	Costco Wholesale	Großhandel	Vereinigte Staaten
CSX	CSX Corporation	Eisenbahnunternehmen	Vereinigte Staaten
CTRP	Ctrip	Touristik	Volksrepublik China
DLTR	Dollar Tree	Billig-Gemischtwarenläden	Vereinigte Staaten
EBAY	eBay	Internetauktionshaus	Vereinigte Staaten
EA	Electronic Arts	Computer- und Videospiele	Vereinigte Staaten
EXPE	Expedia	Online-Touristik	Vereinigte Staaten
FB	Facebook	Soziale Medien	Vereinigte Staaten
FAST	Fastenal	Einzelhandel	Vereinigte Staaten
FISV	Fiserv	Zahlungsverkehr	Vereinigte Staaten
FOX, FOXA	Fox Corporation	Medien	Vereinigte Staaten
GILD	Gilead Sciences	Biotechnologie	Vereinigte Staaten
HAS	Hasbro	Spieleverlag	Vereinigte Staaten
HSIC	Henry Schein	Handel	Vereinigte Staaten
IDXX	Idexx Laboratories	Veterinär-Medizin	Vereinigte Staaten
ILMN	Illumina	Biotechnologie	Vereinigte Staaten
INCY	Incyte	Pharmaunternehmen	Vereinigte Staaten
INTC	Intel	Halbleiter	Vereinigte Staaten
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
