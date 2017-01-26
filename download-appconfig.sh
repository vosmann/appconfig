#!/usr/bin/env bash

DOWNLOAD_PERIOD_IN_SECONDS=${1:-10}
DOWNLOAD_LOCATION=${2:-/tmp/appconfig.json}
APP_CONFIG_URL=${3:-https://raw.githubusercontent.com/vosmann/appconfig/master/src/test/resources/appconfig.json}

while true; do
    curl --silent $APP_CONFIG_URL > $DOWNLOAD_LOCATION
    TIME=`date +%Y-%m-%d:%H:%M:%S`
    echo $TIME Downloaded appconfig.json to $DOWNLOAD_LOCATION.
    sleep $DOWNLOAD_PERIOD_IN_SECONDS
done
