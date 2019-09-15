#!/bin/bash

docker network create influxdb

# influxdb
# The following ports are important and are used by InfluxDB.
# 8086 HTTP API port
# 8083 Administrator interface port, if it is enabled
## 2003 Graphite support, if it is enabled
#
mkdir -p $dataRoot/influxdb
docker run -d --name=influxdb \
    --net=influxdb \
     -v $dataRoot/influxdb:/var/lib/influxdb \
     -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro \
     influxdb:1.7
#    -p 8086:8086

# chronograf
# see https://docs.docker.com/samples/library/chronograf/
mkdir -p $dataRoot/chronograf
docker run -d --name=chronograf \
      -p 8888:8888 \
      --net=influxdb \
      -v $dataRoot/chronograf:/var/lib/chronograf \
      chronograf:1.7 --influxdb-url=http://influxdb:8086

mkdir -p $dataRoot/kapacitor
docker run -d --name=kapacitor \
    -p 9092:9092 \
    -h kapacitor \
    --net=influxdb \
    -v $dataRoot/kapacitor:/var/lib/kapacitor \
    -e KAPACITOR_INFLUXDB_0_URLS_0=http://influxdb:8086 \
    kapacitor:1.5

mkdir -p $dataRoot/telegraf
docker run -d --name=telegraf \
      --net=influxdb \
      -v $PWD/telegraf.conf:/etc/telegraf/telegraf.conf:ro \
      telegraf:1.12
