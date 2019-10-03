#!/bin/bash

base="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
dataRoot="$base/runtime-data"


mkdir -p "$dataRoot/postgresql"
docker run -d --name=postgres \
    -p 5432:5432 \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=WmJLq5T3bKzw \
    -e POSTGRES_DB=mmon \
    -v "$base/postgresql.conf":/etc/postgresql/postgresql.conf \
    -v "$dataRoot/postgresql":/var/lib/postgresql \
    postgres:11 -c 'config_file=/etc/postgresql/postgresql.conf'


docker network create influxdb

# influxdb
# The following ports are important and are used by InfluxDB.
# 8086 HTTP API port
# 8083 Administrator interface port, if it is enabled
## 2003 Graphite support, if it is enabled
#
mkdir -p "$dataRoot/influxdb"
docker run -d --name=influxdb \
    --net=influxdb \
     -p 8086:8086 \
     -v "$dataRoot/influxdb":/var/lib/influxdb \
     -v "$base/influxdb.conf":/etc/influxdb/influxdb.conf:ro \
     influxdb:1.7 -config /etc/influxdb/influxdb.conf

# notes:
# database name: mmon

# chronograf
# see https://docs.docker.com/samples/library/chronograf/
mkdir -p "$dataRoot/chronograf"
docker run -d --name=chronograf \
      -p 8888:8888 \
      --net=influxdb \
      -v "$dataRoot/chronograf":/var/lib/chronograf \
      chronograf:1.7 --influxdb-url=http://influxdb:8086

#mkdir -p "$dataRoot/kapacitor"
#docker run -d --name=kapacitor \
#    -h kapacitor \
#    --net=influxdb \
#    -v "$dataRoot/kapacitor:/var/lib/kapacitor" \
#    -e KAPACITOR_INFLUXDB_0_URLS_0=http://influxdb:8086 \
#    kapacitor:1.5
##    -p 9092:9092 \
#
#mkdir -p "$dataRoot/telegraf"
#docker run -d --name=telegraf \
#      --net=influxdb \
#      -v "$base/telegraf.conf:/etc/telegraf/telegraf.conf:ro" \
#      telegraf:1.12

docker run -d --name=grafana \
  -p 3000:3000 \
  --net=influxdb \
  grafana/grafana \
#  -e "GF_SERVER_ROOT_URL=http://grafana.server.name" \
#  -e "GF_SECURITY_ADMIN_PASSWORD=secret" \
