#!/bin/bash

TESTDATA_DATE="2019-10-20"
TODAY=$(date "+%Y-%m-%d")
MMON_CACHE_FOLDER=/tmp/mmon-cache

mkdir -p $MMON_CACHE_FOLDER

for x in ../resources/mmon-cache/*$TESTDATA_DATE.rawdata; do
  newName=$(basename $x|sed -e "s/$TESTDATA_DATE/$TODAY/")
  cp -v "$x" "$MMON_CACHE_FOLDER/$newName"
done
