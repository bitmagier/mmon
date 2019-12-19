TODO
=

coding
-
* Instead of filling missing week-days in quotes, use the last available stock market day value calculation the daily diff

* provide a config option to use up to x days older cached data too (default=3) 

* Continously running / incremental update mode

* Online-Update mechanism for BootstrapData (e.g. S&P 500 members)
    => https://datahub.io/core/s-and-p-500-companies#resource-s-and-p-500-companies_zip

* Create bigger (unified) company base
    * include major indicies of other trade markets under a common data model
    * add further potentially interesting attributes to companies  

* Support a list of API-Keys to be used in round robin

* When API-Rate-limit is solved:
    * Enable the full S&P 500 base 



other
-
- Secure access setup (HTTPS + Authentification) for grafana production

- Solution for API rate limit [premium api-key costs $29.99/month ;-)]
