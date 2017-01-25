DOWNLOAD_PERIOD_IN_SECONDS=${1:-10}
DOWNLOAD_LOCATION=${2:-/tmp/appconfig.json}

while true; do
    knife node show <STAGING_NODE_NAME> --long > $DOWNLOAD_LOCATION
    echo Downloaded config from server.
    sleep $DOWNLOAD_PERIOD_IN_SECONDS
done
