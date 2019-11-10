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

Grafana
---
When using the docker based development environment, 
the Grafana frontend (not yet configured) is available under http://localhost:3000
Default credentials are "admin/admin"

As a datasource please use: InfluxDB at http://influxdb:8086 with database "mmon". 

Then create a dashboard with panels there using the available data

Already working dashboards can be found under __src/main/grafana__
Please note: Grafana frontend is still work in progress - but you can create your own dashboards on the available data.
 
 
Requirements
------------
- docker
- sbt
- internet connection 

