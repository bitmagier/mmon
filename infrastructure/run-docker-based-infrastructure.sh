#!/bin/basjh

export base=$PWD
export dataRoot=$base/runtime-data

./run-influxdb-docker.sh
./run-grafana-docker.sh
