#!/bin/bash
docker run \
  -d \
  -p 3000:3000 \
  --name=grafana \
  grafana/grafana

#  -e "GF_SERVER_ROOT_URL=http://grafana.server.name" \
#  -e "GF_SECURITY_ADMIN_PASSWORD=secret" \
