#!/bin/bash

base="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
dataRoot="$base/runtime-data"

docker network create influxdb

# influxdb
# The following ports are important and are used by InfluxDB.
# 8086 HTTP API port
# 8083 Administrator interface port, if it is enabled
## 2003 Graphite support, if it is enabled
mkdir -p "$dataRoot/influxdb"
docker run -d --name=influxdb \
    --net=influxdb \
     -p 8086:8086 \
     -v "$dataRoot/influxdb":/var/lib/influxdb \
     -v "$base/influxdb.conf":/etc/influxdb/influxdb.conf:ro \
     influxdb:1.7 -config /etc/influxdb/influxdb.conf

# notes:
# database name: mmon

docker run -d --name=grafana \
  -p 3000:3000 \
  --net=influxdb \
  grafana/grafana
#  -e "GF_SERVER_ROOT_URL=http://grafana.server.name" \
#  -e "GF_SECURITY_ADMIN_PASSWORD=secret" \
