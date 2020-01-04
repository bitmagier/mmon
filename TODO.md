TODO
=

coding
-
* provide a config option to use up to x days older cached data too (default=3) 

* Continously running / incremental update mode

* Online-Update mechanism for BootstrapData (e.g. S&P 500 members)
    => https://datahub.io/core/s-and-p-500-companies#resource-s-and-p-500-companies_zip

* Create bigger (unified) company base
    * include major indicies of other trade markets under a common data model
    * add further potentially interesting attributes to companies  
    * check/find source to load quotes

* Support a list of API-Keys to be used in round robin

* Automate creation of grafana dashboard(s)

other
-
- Secure access setup (HTTPS + Authentification) for grafana production

We need a more solid Quote-API. Current limitations:
-
  - Arbitrary failures: Sometimes a company-symbol from S&P 500 cannot be retrieved any more
  - API rate limit [premium api-key costs $29.99/month ;-)]

