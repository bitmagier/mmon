#!/bin/bash

docker stop grafana
docker rm grafana

docker stop chronograf
docker rm chronograf

docker stop influxdb
docker rm influxdb

docker network rm influxdb