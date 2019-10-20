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

Grafana frontend (not yet configured) is available under http://localhost:3000
Feel free to create a dashboard there using the available data

But: Grafana is not yet configured - this is work in progress - but you can create your own dashboards on the available data
 
Requirements
------------
- docker
- sbt
- internet connection 

