package org.purevalue.mmon

sealed abstract class Sector(val name:String)
object Sector {
  case object Industrials extends Sector("Industrials")
  case object Healthcare extends Sector("Healthcare")
  case object IT extends Sector("IT")
  case object ConsumerDiscretionary extends Sector("ConsumerDiscretionary")
  case object Financials extends Sector("Financials")
  case object Materials extends Sector("Materials")
  case object RealEstate extends Sector("RealEstate")
  case object ConsumerStaples extends Sector("ConsumerStaples")
  case object Utilities extends Sector("Utilities")
  case object Energy extends Sector("Energy")
  case object TelecommunicationServices extends Sector ("TelecommunicationServices")
}
import Sector._

object BootstrapData {
  val sp500Companies: List[Company] = List(
    Company("MMM", "3M Company", Industrials),
    Company("AOS", "A.O. Smith Corp", Industrials),
    Company("ABT", "Abbott Laboratories", Healthcare),
    Company("ABBV", "AbbVie Inc.", Healthcare),
    Company("ACN", "Accenture plc", IT),
    Company("ATVI", "Activision Blizzard", IT),
    Company("AYI", "Acuity Brands Inc", Industrials),
    Company("ADBE", "Adobe Systems Inc", IT),
    Company("AAP", "Advance Auto Parts", ConsumerDiscretionary),
    Company("AMD", "Advanced Micro Devices Inc", IT),
    Company("AES", "AES Corp", Utilities),
    Company("AET", "Aetna Inc", Healthcare),
    Company("AMG", "Affiliated Managers Group Inc", Financials),
    Company("AFL", "AFLAC Inc", Financials),
    Company("A", "Agilent Technologies Inc", Healthcare),
    Company("APD", "Air Products & Chemicals Inc", Materials),
    Company("AKAM", "Akamai Technologies Inc", IT),
    Company("ALK", "Alaska Air Group Inc", Industrials),
    Company("ALB", "Albemarle Corp", Materials),
    Company("ARE", "Alexandria Real Estate Equities Inc", RealEstate),
    Company("ALXN", "Alexion Pharmaceuticals", Healthcare),
    Company("ALGN", "Align Technology", Healthcare),
    Company("ALLE", "Allegion", Industrials),
    Company("AGN", "Allergan, Plc", Healthcare),
    Company("ADS", "Alliance Data Systems", IT),
    Company("LNT", "Alliant Energy Corp", Utilities),
    Company("ALL", "Allstate Corp", Financials),
    Company("GOOGL", "Alphabet Inc Class A", IT),
    //    Company("GOOG", "Alphabet Inc Class C", IT),
    Company("MO", "Altria Group Inc", ConsumerStaples),
    Company("AMZN", "Amazon.com Inc.", ConsumerDiscretionary),
    Company("AEE", "Ameren Corp", Utilities),
    Company("AAL", "American Airlines Group", Industrials),
    Company("AEP", "American Electric Power", Utilities),
    Company("AXP", "American Express Co", Financials),
    Company("AIG", "American International Group, Inc.", Financials),
    Company("AMT", "American Tower Corp A", RealEstate),
    Company("AWK", "American Water Works Company Inc", Utilities),
    Company("AMP", "Ameriprise Financial", Financials),
    Company("ABC", "AmerisourceBergen Corp", Healthcare),
    Company("AME", "AMETEK Inc.", Industrials),
    Company("AMGN", "Amgen Inc.", Healthcare),
    Company("APH", "Amphenol Corp", IT),
    Company("APC", "Anadarko Petroleum Corp", Energy),
    Company("ADI", "Analog Devices, Inc.", IT),
    Company("ANDV", "Andeavor", Energy),
    Company("ANSS", "ANSYS", IT),
    Company("ANTM", "Anthem Inc.", Healthcare),
    Company("AON", "Aon plc", Financials),
    Company("APA", "Apache Corporation", Energy),
    Company("AIV", "Apartment Investment & Management", RealEstate),
    Company("AAPL", "Apple Inc.", IT),
    Company("AMAT", "Applied Materials Inc.", IT),
    Company("APTV", "Aptiv Plc", ConsumerDiscretionary),
    Company("ADM", "Archer-Daniels-Midland Co", ConsumerStaples),
    Company("ARNC", "Arconic Inc.", Industrials),
    Company("AJG", "Arthur J. Gallagher & Co.", Financials),
    Company("AIZ", "Assurant Inc.", Financials),
    Company("T", "AT&T Inc.", TelecommunicationServices),
    Company("ADSK", "Autodesk Inc.", IT),
    Company("ADP", "Automatic Data Processing", IT),
    Company("AZO", "AutoZone Inc", ConsumerDiscretionary),
    Company("AVB", "AvalonBay Communities, Inc.", RealEstate),
    Company("AVY", "Avery Dennison Corp", Materials),
    Company("BHGE", "Baker Hughes, a GE Company", Energy),
    Company("BLL", "Ball Corp", Materials),
    Company("BAC", "Bank of America Corp", Financials),
    Company("BAX", "Baxter International Inc.", Healthcare),
    Company("BBT", "BB&T Corporation", Financials),
    Company("BDX", "Becton Dickinson", Healthcare),
    Company("BRK.B", "Berkshire Hathaway", Financials),
    Company("BBY", "Best Buy Co. Inc.", ConsumerDiscretionary),
    Company("BIIB", "Biogen Inc.", Healthcare),
    Company("BLK", "BlackRock", Financials),
    Company("HRB", "Block H&R", Financials),
    Company("BA", "Boeing Company", Industrials),
    Company("BKNG", "Booking Holdings Inc", ConsumerDiscretionary),
    Company("BWA", "BorgWarner", ConsumerDiscretionary),
    Company("BXP", "Boston Properties", RealEstate),
    Company("BSX", "Boston Scientific", Healthcare),
    Company("BHF", "Brighthouse Financial Inc", Financials),
    Company("BMY", "Bristol-Myers Squibb", Healthcare),
    Company("AVGO", "Broadcom", IT),
    Company("BF.B", "Brown-Forman Corp.", ConsumerStaples),
    Company("CHRW", "C. H. Robinson Worldwide", Industrials),
    Company("CA", "CA, Inc.", IT),
    Company("COG", "Cabot Oil & Gas", Energy),
    Company("CDNS", "Cadence Design Systems", IT),
    Company("CPB", "Campbell Soup", ConsumerStaples),
    Company("COF", "Capital One Financial", Financials),
    Company("CAH", "Cardinal Health Inc.", Healthcare),
    Company("KMX", "Carmax Inc", ConsumerDiscretionary),
    Company("CCL", "Carnival Corp.", ConsumerDiscretionary),
    Company("CAT", "Caterpillar Inc.", Industrials),
    Company("CBOE", "Cboe Global Markets", Financials),
    Company("CBRE", "CBRE Group", RealEstate),
    Company("CBS", "CBS Corp.", ConsumerDiscretionary),
    Company("CELG", "Celgene Corp.", Healthcare),
    Company("CNC", "Centene Corporation", Healthcare),
    Company("CNP", "CenterPoint Energy", Utilities),
    Company("CTL", "CenturyLink Inc", TelecommunicationServices),
    Company("CERN", "Cerner", Healthcare),
    Company("CF", "CF Industries Holdings Inc", Materials),
    Company("SCHW", "Charles Schwab Corporation", Financials),
    Company("CHTR", "Charter Communications", ConsumerDiscretionary),
    Company("CVX", "Chevron Corp.", Energy),
    Company("CMG", "Chipotle Mexican Grill", ConsumerDiscretionary),
    Company("CB", "Chubb Limited", Financials),
    Company("CHD", "Church & Dwight", ConsumerStaples),
    Company("CI", "CIGNA Corp.", Healthcare),
    Company("XEC", "Cimarex Energy", Energy),
    Company("CINF", "Cincinnati Financial", Financials),
    Company("CTAS", "Cintas Corporation", Industrials),
    Company("CSCO", "Cisco Systems", IT),
    Company("C", "Citigroup Inc.", Financials),
    Company("CFG", "Citizens Financial Group", Financials),
    Company("CTXS", "Citrix Systems", IT),
    Company("CME", "CME Group Inc.", Financials),
    Company("CMS", "CMS Energy", Utilities),
    Company("KO", "Coca-Cola Company (The)", ConsumerStaples),
    Company("CTSH", "Cognizant Technology Solutions", IT),
    Company("CL", "Colgate-Palmolive", ConsumerStaples),
    Company("CMCSA", "Comcast Corp.", ConsumerDiscretionary),
    Company("CMA", "Comerica Inc.", Financials),
    Company("CAG", "Conagra Brands", ConsumerStaples),
    Company("CXO", "Concho Resources", Energy),
    Company("COP", "ConocoPhillips", Energy),
    Company("ED", "Consolidated Edison", Utilities),
    Company("STZ", "Constellation Brands", ConsumerStaples),
    Company("GLW", "Corning Inc.", IT),
    Company("COST", "Costco Wholesale Corp.", ConsumerStaples),
    Company("COTY", "Coty, Inc", ConsumerStaples),
    Company("CCI", "Crown Castle International Corp.", RealEstate),
    Company("CSRA", "CSRA Inc.", IT),
    Company("CSX", "CSX Corp.", Industrials),
    Company("CMI", "Cummins Inc.", Industrials),
    Company("CVS", "CVS Health", ConsumerStaples),
    Company("DHI", "D. R. Horton", ConsumerDiscretionary),
    Company("DHR", "Danaher Corp.", Healthcare),
    Company("DRI", "Darden Restaurants", ConsumerDiscretionary),
    Company("DVA", "DaVita Inc.", Healthcare),
    Company("DE", "Deere & Co.", Industrials),
    Company("DAL", "Delta Air Lines Inc.", Industrials),
    Company("XRAY", "Dentsply Sirona", Healthcare),
    Company("DVN", "Devon Energy Corp.", Energy),
    Company("DLR", "Digital Realty Trust Inc", RealEstate),
    Company("DFS", "Discover Financial Services", Financials),
    Company("DISCA", "Discovery Inc. Class A", ConsumerDiscretionary),
    //  Company("DISCK", "Discovery Inc. Class C", ConsumerDiscretionary),
    Company("DISH", "Dish Network", ConsumerDiscretionary),
    Company("DG", "Dollar General", ConsumerDiscretionary),
    Company("DLTR", "Dollar Tree", ConsumerDiscretionary),
    Company("D", "Dominion Energy", Utilities),
    Company("DOV", "Dover Corp.", Industrials),
    Company("DWDP", "DowDuPont", Materials),
    Company("DPS", "Dr Pepper Snapple Group", ConsumerStaples),
    Company("DTE", "DTE Energy Co.", Utilities),
    Company("DUK", "Duke Energy", Utilities),
    Company("DRE", "Duke Realty Corp", RealEstate),
    Company("DXC", "DXC Technology", IT),
    Company("ETFC", "E*Trade", Financials),
    Company("EMN", "Eastman Chemical", Materials),
    Company("ETN", "Eaton Corporation", Industrials),
    Company("EBAY", "eBay Inc.", IT),
    Company("ECL", "Ecolab Inc.", Materials),
    Company("EIX", "Edison Int'l", Utilities),
    Company("EW", "Edwards Lifesciences", Healthcare),
    Company("EA", "Electronic Arts", IT),
    Company("EMR", "Emerson Electric Company", Industrials),
    Company("ETR", "Entergy Corp.", Utilities),
    Company("EVHC", "Envision Healthcare", Healthcare),
    Company("EOG", "EOG Resources", Energy),
    Company("EQT", "EQT Corporation", Energy),
    Company("EFX", "Equifax Inc.", Industrials),
    Company("EQIX", "Equinix", RealEstate),
    Company("EQR", "Equity Residential", RealEstate),
    Company("ESS", "Essex Property Trust, Inc.", RealEstate),
    Company("EL", "Estee Lauder Cos.", ConsumerStaples),
    Company("RE", "Everest Re Group Ltd.", Financials),
    Company("ES", "Eversource Energy", Utilities),
    Company("EXC", "Exelon Corp.", Utilities),
    Company("EXPE", "Expedia Inc.", ConsumerDiscretionary),
    Company("EXPD", "Expeditors International", Industrials),
    Company("ESRX", "Express Scripts", Healthcare),
    Company("EXR", "Extra Space Storage", RealEstate),
    Company("XOM", "Exxon Mobil Corp.", Energy),
    Company("FFIV", "F5 Networks", IT),
    Company("FB", "Facebook, Inc.", IT),
    Company("FAST", "Fastenal Co", Industrials),
    Company("FRT", "Federal Realty Investment Trust", RealEstate),
    Company("FDX", "FedEx Corporation", Industrials),
    Company("FIS", "Fidelity National Information Services", IT),
    Company("FITB", "Fifth Third Bancorp", Financials),
    Company("FE", "FirstEnergy Corp", Utilities),
    Company("FISV", "Fiserv Inc", IT),
    Company("FLIR", "FLIR Systems", IT),
    Company("FLS", "Flowserve Corporation", Industrials),
    Company("FLR", "Fluor Corp.", Industrials),
    Company("FMC", "FMC Corporation", Materials),
    Company("FL", "Foot Locker Inc", ConsumerDiscretionary),
    Company("F", "Ford Motor", ConsumerDiscretionary),
    Company("FTV", "Fortive Corp", Industrials),
    Company("FBHS", "Fortune Brands Home & Security", Industrials),
    Company("BEN", "Franklin Resources", Financials),
    Company("FCX", "Freeport-McMoRan Inc.", Materials),
    Company("GPS", "Gap Inc.", ConsumerDiscretionary),
    Company("GRMN", "Garmin Ltd.", ConsumerDiscretionary),
    Company("IT", "Gartner Inc", IT),
    Company("GD", "General Dynamics", Industrials),
    Company("GE", "General Electric", Industrials),
    Company("GGP", "General Growth Properties Inc.", RealEstate),
    Company("GIS", "General Mills", ConsumerStaples),
    Company("GM", "General Motors", ConsumerDiscretionary),
    Company("GPC", "Genuine Parts", ConsumerDiscretionary),
    Company("GILD", "Gilead Sciences", Healthcare),
    Company("GPN", "Global Payments Inc.", IT),
    Company("GS", "Goldman Sachs Group", Financials),
    Company("GT", "Goodyear Tire & Rubber", ConsumerDiscretionary),
    Company("GWW", "Grainger (W.W.) Inc.", Industrials),
    Company("HAL", "Halliburton Co.", Energy),
    Company("HBI", "Hanesbrands Inc", ConsumerDiscretionary),
    Company("HOG", "Harley-Davidson", ConsumerDiscretionary),
    Company("HRS", "Harris Corporation", IT),
    Company("HIG", "Hartford Financial Svc.Gp.", Financials),
    Company("HAS", "Hasbro Inc.", ConsumerDiscretionary),
    Company("HCA", "HCA Holdings", Healthcare),
    Company("HCP", "HCP Inc.", RealEstate),
    Company("HP", "Helmerich & Payne", Energy),
    Company("HSIC", "Henry Schein", Healthcare),
    Company("HES", "Hess Corporation", Energy),
    Company("HPE", "Hewlett Packard Enterprise", IT),
    Company("HLT", "Hilton Worldwide Holdings Inc", ConsumerDiscretionary),
    Company("HOLX", "Hologic", Healthcare),
    Company("HD", "Home Depot", ConsumerDiscretionary),
    Company("HON", "Honeywell Int'l Inc.", Industrials),
    Company("HRL", "Hormel Foods Corp.", ConsumerStaples),
    Company("HST", "Host Hotels & Resorts", RealEstate),
    Company("HPQ", "HP Inc.", IT),
    Company("HUM", "Humana Inc.", Healthcare),
    Company("HBAN", "Huntington Bancshares", Financials),
    Company("HII", "Huntington Ingalls Industries", Industrials),
    Company("IDXX", "IDEXX Laboratories", Healthcare),
    Company("INFO", "IHS Markit Ltd.", Industrials),
    Company("ITW", "Illinois Tool Works", Industrials),
    Company("ILMN", "Illumina Inc", Healthcare),
    Company("INCY", "Incyte", Healthcare),
    Company("IR", "Ingersoll-Rand PLC", Industrials),
    Company("INTC", "Intel Corp.", IT),
    Company("ICE", "Intercontinental Exchange", Financials),
    Company("IBM", "International Business Machines", IT),
    Company("IP", "International Paper", Materials),
    Company("IPG", "Interpublic Group", ConsumerDiscretionary),
    Company("IFF", "Intl Flavors & Fragrances", Materials),
    Company("INTU", "Intuit Inc.", IT),
    Company("ISRG", "Intuitive Surgical Inc.", Healthcare),
    Company("IVZ", "Invesco Ltd.", Financials),
    Company("IPGP", "IPG Photonics Corp.", IT),
    Company("IQV", "IQVIA Holdings Inc.", Healthcare),
    Company("IRM", "Iron Mountain Incorporated", RealEstate),
    Company("JBHT", "J. B. Hunt Transport Services", Industrials),
    Company("JEC", "Jacobs Engineering Group", Industrials),
    Company("SJM", "JM Smucker", ConsumerStaples),
    Company("JNJ", "Johnson & Johnson", Healthcare),
    Company("JCI", "Johnson Controls International", Industrials),
    Company("JPM", "JPMorgan Chase & Co.", Financials),
    Company("JNPR", "Juniper Networks", IT),
    Company("KSU", "Kansas City Southern", Industrials),
    Company("K", "Kellogg Co.", ConsumerStaples),
    Company("KEY", "KeyCorp", Financials),
    Company("KMB", "Kimberly-Clark", ConsumerStaples),
    Company("KIM", "Kimco Realty", RealEstate),
    Company("KMI", "Kinder Morgan", Energy),
    Company("KLAC", "KLA-Tencor Corp.", IT),
    Company("KSS", "Kohl's Corp.", ConsumerDiscretionary),
    Company("KHC", "Kraft Heinz Co", ConsumerStaples),
    Company("KR", "Kroger Co.", ConsumerStaples),
    Company("LB", "L Brands Inc.", ConsumerDiscretionary),
    Company("LLL", "L-3 Communications Holdings", Industrials),
    Company("LH", "Laboratory Corp. of America Holding", Healthcare),
    Company("LRCX", "Lam Research", IT),
    Company("LEG", "Leggett & Platt", ConsumerDiscretionary),
    Company("LEN", "Lennar Corp.", ConsumerDiscretionary),
    Company("LUK", "Leucadia National Corp.", Financials),
    Company("LLY", "Lilly (Eli) & Co.", Healthcare),
    Company("LNC", "Lincoln National", Financials),
    Company("LKQ", "LKQ Corporation", ConsumerDiscretionary),
    Company("LMT", "Lockheed Martin Corp.", Industrials),
    Company("L", "Loews Corp.", Financials),
    Company("LOW", "Lowe's Cos.", ConsumerDiscretionary),
    Company("LYB", "LyondellBasell", Materials),
    Company("MTB", "M&T Bank Corp.", Financials),
    Company("MAC", "Macerich", RealEstate),
    Company("M", "Macy's Inc.", ConsumerDiscretionary),
    Company("MRO", "Marathon Oil Corp.", Energy),
    Company("MPC", "Marathon Petroleum", Energy),
    Company("MAR", "Marriott Int'l.", ConsumerDiscretionary),
    Company("MMC", "Marsh & McLennan", Financials),
    Company("MLM", "Martin Marietta Materials", Materials),
    Company("MAS", "Masco Corp.", Industrials),
    Company("MA", "Mastercard Inc.", IT),
    Company("MAT", "Mattel Inc.", ConsumerDiscretionary),
    Company("MKC", "McCormick & Co.", ConsumerStaples),
    Company("MCD", "McDonald's Corp.", ConsumerDiscretionary),
    Company("MCK", "McKesson Corp.", Healthcare),
    Company("MDT", "Medtronic plc", Healthcare),
    Company("MRK", "Merck & Co.", Healthcare),
    Company("MET", "MetLife Inc.", Financials),
    Company("MTD", "Mettler Toledo", Healthcare),
    Company("MGM", "MGM Resorts International", ConsumerDiscretionary),
    Company("KORS", "Michael Kors Holdings", ConsumerDiscretionary),
    Company("MCHP", "Microchip Technology", IT),
    Company("MU", "Micron Technology", IT),
    Company("MSFT", "Microsoft Corp.", IT),
    Company("MAA", "Mid-America Apartments", RealEstate),
    Company("MHK", "Mohawk Industries", ConsumerDiscretionary),
    Company("TAP", "Molson Coors Brewing Company", ConsumerStaples),
    Company("MDLZ", "Mondelez International", ConsumerStaples),
    Company("MON", "Monsanto Co.", Materials),
    Company("MNST", "Monster Beverage", ConsumerStaples),
    Company("MCO", "Moody's Corp", Financials),
    Company("MS", "Morgan Stanley", Financials),
    Company("MSI", "Motorola Solutions Inc.", IT),
    Company("MYL", "Mylan N.V.", Healthcare),
    Company("NDAQ", "Nasdaq, Inc.", Financials),
    Company("NOV", "National Oilwell Varco Inc.", Energy),
    Company("NAVI", "Navient", Financials),
    Company("NKTR", "Nektar Therapeutics", Healthcare),
    Company("NTAP", "NetApp", IT),
    Company("NFLX", "Netflix Inc.", IT),
    Company("NWL", "Newell Brands", ConsumerDiscretionary),
    Company("NFX", "Newfield Exploration Co", Energy),
    Company("NEM", "Newmont Mining Corporation", Materials),
    Company("NWSA", "News Corp. Class A", ConsumerDiscretionary),
    //  Company("NWS", "News Corp. Class B", ConsumerDiscretionary),
    Company("NEE", "NextEra Energy", Utilities),
    Company("NLSN", "Nielsen Holdings", Industrials),
    Company("NKE", "Nike", ConsumerDiscretionary),
    Company("NI", "NiSource Inc.", Utilities),
    Company("NBL", "Noble Energy Inc", Energy),
    Company("JWN", "Nordstrom", ConsumerDiscretionary),
    Company("NSC", "Norfolk Southern Corp.", Industrials),
    Company("NTRS", "Northern Trust Corp.", Financials),
    Company("NOC", "Northrop Grumman Corp.", Industrials),
    Company("NCLH", "Norwegian Cruise Line", ConsumerDiscretionary),
    Company("NRG", "NRG Energy", Utilities),
    Company("NUE", "Nucor Corp.", Materials),
    Company("NVDA", "Nvidia Corporation", IT),
    Company("ORLY", "O'Reilly Automotive", ConsumerDiscretionary),
    Company("OXY", "Occidental Petroleum", Energy),
    Company("OMC", "Omnicom Group", ConsumerDiscretionary),
    Company("OKE", "ONEOK", Energy),
    Company("ORCL", "Oracle Corp.", IT),
    Company("PCAR", "PACCAR Inc.", Industrials),
    Company("PKG", "Packaging Corporation of America", Materials),
    Company("PH", "Parker-Hannifin", Industrials),
    Company("PAYX", "Paychex Inc.", IT),
    Company("PYPL", "PayPal", IT),
    Company("PNR", "Pentair Ltd.", Industrials),
    Company("PBCT", "People's United Financial", Financials),
    Company("PEP", "PepsiCo Inc.", ConsumerStaples),
    Company("PKI", "PerkinElmer", Healthcare),
    Company("PRGO", "Perrigo", Healthcare),
    Company("PFE", "Pfizer Inc.", Healthcare),
    Company("PCG", "PG&E Corp.", Utilities),
    Company("PM", "Philip Morris International", ConsumerStaples),
    Company("PSX", "Phillips 66", Energy),
    Company("PNW", "Pinnacle West Capital", Utilities),
    Company("PXD", "Pioneer Natural Resources", Energy),
    Company("PNC", "PNC Financial Services", Financials),
    Company("RL", "Polo Ralph Lauren Corp.", ConsumerDiscretionary),
    Company("PPG", "PPG Industries", Materials),
    Company("PPL", "PPL Corp.", Utilities),
    Company("PX", "Praxair Inc.", Materials),
    Company("PFG", "Principal Financial Group", Financials),
    Company("PG", "Procter & Gamble", ConsumerStaples),
    Company("PGR", "Progressive Corp.", Financials),
    Company("PLD", "Prologis", RealEstate),
    Company("PRU", "Prudential Financial", Financials),
    Company("PEG", "Public Serv. Enterprise Inc.", Utilities),
    Company("PSA", "Public Storage", RealEstate),
    Company("PHM", "Pulte Homes Inc.", ConsumerDiscretionary),
    Company("PVH", "PVH Corp.", ConsumerDiscretionary),
    Company("QRVO", "Qorvo", IT),
    Company("QCOM", "QUALCOMM Inc.", IT),
    Company("PWR", "Quanta Services Inc.", Industrials),
    Company("DGX", "Quest Diagnostics", Healthcare),
    Company("RRC", "Range Resources Corp.", Energy),
    Company("RJF", "Raymond James Financial Inc.", Financials),
    Company("RTN", "Raytheon Co.", Industrials),
    Company("O", "Realty Income Corporation", RealEstate),
    Company("RHT", "Red Hat Inc.", IT),
    Company("REG", "Regency Centers Corporation", RealEstate),
    Company("REGN", "Regeneron", Healthcare),
    Company("RF", "Regions Financial Corp.", Financials),
    Company("RSG", "Republic Services Inc", Industrials),
    Company("RMD", "ResMed", Healthcare),
    Company("RHI", "Robert Half International", Industrials),
    Company("ROK", "Rockwell Automation Inc.", Industrials),
    Company("COL", "Rockwell Collins", Industrials),
    Company("ROP", "Roper Technologies", Industrials),
    Company("ROST", "Ross Stores", ConsumerDiscretionary),
    Company("RCL", "Royal Caribbean Cruises Ltd", ConsumerDiscretionary),
    Company("SPGI", "S&P Global, Inc.", Financials),
    Company("CRM", "Salesforce.com", IT),
    Company("SBAC", "SBA Communications", RealEstate),
    Company("SCG", "SCANA Corp", Utilities),
    Company("SLB", "Schlumberger Ltd.", Energy),
    Company("STX", "Seagate Technology", IT),
    Company("SEE", "Sealed Air", Materials),
    Company("SRE", "Sempra Energy", Utilities),
    Company("SHW", "Sherwin-Williams", Materials),
    Company("SPG", "Simon Property Group Inc", RealEstate),
    Company("SWKS", "Skyworks Solutions", IT),
    Company("SLG", "SL Green Realty", RealEstate),
    Company("SNA", "Snap-On Inc.", ConsumerDiscretionary),
    Company("SO", "Southern Co.", Utilities),
    Company("LUV", "Southwest Airlines", Industrials),
    Company("SWK", "Stanley Black & Decker", ConsumerDiscretionary),
    Company("SBUX", "Starbucks Corp.", ConsumerDiscretionary),
    Company("STT", "State Street Corp.", Financials),
    Company("SRCL", "Stericycle Inc", Industrials),
    Company("SYK", "Stryker Corp.", Healthcare),
    Company("STI", "SunTrust Banks", Financials),
    Company("SIVB", "SVB Financial", Financials),
    Company("SYMC", "Symantec Corp.", IT),
    Company("SYF", "Synchrony Financial", Financials),
    Company("SNPS", "Synopsys Inc.", IT),
    Company("SYY", "Sysco Corp.", ConsumerStaples),
    Company("TROW", "T. Rowe Price Group", Financials),
    Company("TTWO", "Take-Two Interactive", IT),
    Company("TPR", "Tapestry, Inc.", ConsumerDiscretionary),
    Company("TGT", "Target Corp.", ConsumerDiscretionary),
    Company("TEL", "TE Connectivity Ltd.", IT),
    Company("FTI", "TechnipFMC", Energy),
    Company("TXN", "Texas Instruments", IT),
    Company("TXT", "Textron Inc.", Industrials),
    Company("BK", "The Bank of New York Mellon Corp.", Financials),
    Company("CLX", "The Clorox Company", ConsumerStaples),
    Company("COO", "The Cooper Companies", Healthcare),
    Company("HSY", "The Hershey Company", ConsumerStaples),
    Company("MOS", "The Mosaic Company", Materials),
    Company("TRV", "The Travelers Companies Inc.", Financials),
    Company("DIS", "The Walt Disney Company", ConsumerDiscretionary),
    Company("TMO", "Thermo Fisher Scientific", Healthcare),
    Company("TIF", "Tiffany & Co.", ConsumerDiscretionary),
    Company("TWX", "Time Warner Inc.", ConsumerDiscretionary),
    Company("TJX", "TJX Companies Inc.", ConsumerDiscretionary),
    Company("TMK", "Torchmark Corp.", Financials),
    Company("TSS", "Total System Services", IT),
    Company("TSCO", "Tractor Supply Company", ConsumerDiscretionary),
    Company("TDG", "TransDigm Group", Industrials),
    Company("TRIP", "TripAdvisor", ConsumerDiscretionary),
    Company("FOXA", "Twenty-First Century Fox Class A", ConsumerDiscretionary),
    //  Company("FOX", "Twenty-First Century Fox Class B", ConsumerDiscretionary),
    Company("TSN", "Tyson Foods", ConsumerStaples),
    Company("USB", "U.S. Bancorp", Financials),
    Company("UDR", "UDR Inc", RealEstate),
    Company("ULTA", "Ulta Beauty", ConsumerDiscretionary),
    Company("UAA", "Under Armour Class A", ConsumerDiscretionary),
    //  Company("UA", "Under Armour Class C", ConsumerDiscretionary),
    Company("UNP", "Union Pacific", Industrials),
    Company("UAL", "United Continental Holdings", Industrials),
    Company("UNH", "United Health Group Inc.", Healthcare),
    Company("UPS", "United Parcel Service", Industrials),
    Company("URI", "United Rentals, Inc.", Industrials),
    Company("UTX", "United Technologies", Industrials),
    Company("UHS", "Universal Health Services, Inc.", Healthcare),
    Company("UNM", "Unum Group", Financials),
    Company("VFC", "V.F. Corp.", ConsumerDiscretionary),
    Company("VLO", "Valero Energy", Energy),
    Company("VAR", "Varian Medical Systems", Healthcare),
    Company("VTR", "Ventas Inc", RealEstate),
    Company("VRSN", "Verisign Inc.", IT),
    Company("VRSK", "Verisk Analytics", Industrials),
    Company("VZ", "Verizon Communications", TelecommunicationServices),
    Company("VRTX", "Vertex Pharmaceuticals Inc", Healthcare),
    Company("VIAB", "Viacom Inc.", ConsumerDiscretionary),
    Company("V", "Visa Inc.", IT),
    Company("VNO", "Vornado Realty Trust", RealEstate),
    Company("VMC", "Vulcan Materials", Materials),
    Company("WMT", "Wal-Mart Stores", ConsumerStaples),
    Company("WBA", "Walgreens Boots Alliance", ConsumerStaples),
    Company("WM", "Waste Management Inc.", Industrials),
    Company("WAT", "Waters Corporation", Healthcare),
    Company("WEC", "Wec Energy Group Inc", Utilities),
    Company("WFC", "Wells Fargo", Financials),
    Company("WELL", "Welltower Inc.", RealEstate),
    Company("WDC", "Western Digital", IT),
    Company("WU", "Western Union Co", IT),
    Company("WRK", "WestRock Company", Materials),
    Company("WY", "Weyerhaeuser Corp.", RealEstate),
    Company("WHR", "Whirlpool Corp.", ConsumerDiscretionary),
    Company("WMB", "Williams Cos.", Energy),
    Company("WLTW", "Willis Towers Watson", Financials),
    Company("WYN", "Wyndham Worldwide", ConsumerDiscretionary),
    Company("WYNN", "Wynn Resorts Ltd", ConsumerDiscretionary),
    Company("XEL", "Xcel Energy Inc", Utilities),
    Company("XRX", "Xerox Corp.", IT),
    Company("XLNX", "Xilinx Inc", IT),
    Company("XL", "XL Capital", Financials),
    Company("XYL", "Xylem Inc.", Industrials),
    Company("YUM", "Yum! Brands Inc", ConsumerDiscretionary),
    Company("ZBH", "Zimmer Biomet Holdings", Healthcare),
    Company("ZION", "Zions Bancorp", Financials),
    Company("ZTS", "Zoetis", Healthcare))
}
