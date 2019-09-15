#!/bin/basjh

export dataRoot=$PWD/runtime-data

influxdb/run-influxdb-docker.sh
grafana/run-grafana-docker.sh
