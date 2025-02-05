#!/usr/bin/env bash

source ./.env

FILE="./.env"

# Check if the file exists
if [ ! -f "$FILE" ]; then
    echo "Error: Missing file with settings [.env]" >&2  # Print error message to stderr
    exit 1  # Exit with an error code
fi
source ./.env

AUTH=""
if [ -n "$ES_USERNAME" ]; then
    AUTH=" -k -u $ES_USERNAME:$ES_PASSWORD"
fi

echo "Removing ingest pipeline"
curl -X DELETE "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_ingest/pipeline/recyclone_event_index_ingest_pipeline" $AUTH
echo
echo "Removing template"
curl -X DELETE "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_index_template/recyclone_event_index" $AUTH
echo
echo "Removing mapping"
curl -X DELETE "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_component_template/recyclone_event_index_mapping_1" $AUTH
echo
echo "Removing field mapping"
curl -X DELETE "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_component_template/recyclone_event_index_field_mapping_1" $AUTH
echo
echo "Removing settings"
curl -X DELETE "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_component_template/recyclone_event_index_settings_1" $AUTH
echo
echo "Removing policy"
curl -X DELETE "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_ilm/policy/recyclone_event_index_policy" $AUTH
echo