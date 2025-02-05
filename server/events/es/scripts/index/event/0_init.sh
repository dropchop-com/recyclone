#!/usr/bin/env bash

FILE="./.env"

# Check if the file exists
if [ ! -f "$FILE" ]; then
    echo "
        Missing configuration [.env] file:
          ES_PROTOCOL=
          ES_HOST=
          ES_PORT=
          ES_USERNAME=
          ES_PASSWORD=
    " >&2
    exit 1  # Exit with an error code
fi
source ./.env

AUTH=""
if [ -n "$ES_USERNAME" ]; then
    AUTH=" -k -u $ES_USERNAME:$ES_PASSWORD"
fi

echo "Preparing elasticsearch for recyclone_event index"
echo "Adding ingest pipeline"
curl -X PUT "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_ingest/pipeline/recyclone_event_index_ingest_pipeline" $AUTH -H 'Content-Type: application/json' -d @event-ingest-pipeline.json
echo
echo "Adding mapping"
curl -X PUT "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_component_template/recyclone_event_index_mapping_1" $AUTH -H 'Content-Type: application/json' -d @event-comp-tmpl-index-mapping.json
echo
echo "Adding field mapping"
curl -X PUT "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_component_template/recyclone_event_index_field_mapping_1" $AUTH -H 'Content-Type: application/json' -d @event-comp-tmpl-field-mapping.json
echo
echo "Adding settings"
curl -X PUT "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_component_template/recyclone_event_index_settings_1" $AUTH -H 'Content-Type: application/json' -d @event-comp-tmpl-settings.json
echo
echo "Adding template"
curl -X PUT "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_index_template/recyclone_event_index" $AUTH -H 'Content-Type: application/json' -d @event-index-tmpl.json
echo
echo "Adding policy"
curl -X PUT "$ES_PROTOCOL://$ES_HOST:$ES_PORT/_ilm/policy/recyclone_event_index_policy" $AUTH -H 'Content-Type: application/json' -d @event-policy.json
echo