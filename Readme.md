Market Monitor
==============

Foundation for the development of custom stock market indicators & alarms.
The analysis is based on real stock data imported into an InfluxDB and a Grafana frontend.

Usage
-----
First you need to get your own API-Key from https://www.alphavantage.co/support/#api-key and store it into the configuration file.

Then start the development infrastructure:

    infrastructure/dev/run-docker-based-infrastructure.sh

and the program with:

    sbt "run c"
This will import the available history of the daily quotes of all S&P500 companies into an influx database and create the custom indicators.  

Applying indicators only (to an already created database), can be done by:
    
    sbt "run i" 

Grafana frontend (not yet configured) is available under http://localhost:3000
Default credentials are "admin/admin"
Datasource: InfluxDB at http://localhost:8086
Influx database: mmon 
Feel free to add the datasource and create a dashboard with panels there using the available data

But: Grafana is not yet configured - this is work in progress - but you can create your own dashboards on the available data
 
Requirements
------------
- docker
- sbt
- internet connection 

