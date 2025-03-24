#!/bin/bash

# Absolute path to this script
SCRIPT=$(readlink -f $0)
# Directory of an absolute path to this script
SCRIPTPATH=`dirname $SCRIPT`
ES_HOST=http://localhost:9200
ROLES_USERS=( )
INDICES_FOR_INSERTION=("dummy")

declare -A EVENT_ITEMS=(
  ["_ingest/pipeline/event_index_ingest_pipeline"]="event-ingest-pipeline"
  ["_component_template/event_index_mapping_1"]="event-comp-tmpl-index-mapping"
  ["_component_template/event_index_field_mapping_1"]="event-comp-tmpl-field-mapping"
  ["_component_template/event_index_settings_1"]="event-comp-tmpl-settings"
  ["_ilm/policy/event_index_policy"]="event-policy"
)

declare -A INDICES=(
  ["dummy"]="dummy-index-tpl"
  ["event_index"]="event-index-tpl"
)

ES_AUTH=""
if [[ ! -z "${ELASTIC_PASSWORD}" ]]; then
  printf "\n\n\n\nWill use init password: '${ELASTIC_PASSWORD}' \n\n\n\n"
  ES_AUTH="-u elastic:${ELASTIC_PASSWORD}"
fi

# Wait for cluster to be healthy
until curl ${ES_AUTH} -s "${ES_HOST}/_cluster/health" | grep -q '"status":"green"'; do
    sleep 1
done

# Function to check Elasticsearch path response HTTP status
check_item() {
  local item_path="$1"
  local response=$(curl ${ES_AUTH} -s -o /dev/null -w "%{http_code}" -XGET "${ES_HOST}/${item_path}")
  if [ "$response" -ne 200 ]; then
    printf "\n\n\nINFO: Item '${item_path}' returned HTTP ${response}\n\n\n"
    printf "Will initialize [${item_path}] ...\n"
    return 1
  fi
  printf "\n\n\nINFO: Item '${item_path}' already exists\n\n"
  return 0
}

# Roles and users template initialization commands
for user_role in "${ROLES_USERS[@]}"; do
  if ! check_item "_security/role/${user_role}"; then
    curl ${ES_AUTH} -X POST "${ES_HOST}/_security/role/${user_role}" -H 'Content-Type: application/json' \
      -d @${SCRIPTPATH}/${user_role}-role.json
  fi
  if ! check_item "_security/user/${user_role}"; then
    curl ${ES_AUTH} -X POST "${ES_HOST}/_security/user/${user_role}" -H 'Content-Type: application/json' \
      -d @${SCRIPTPATH}/${user_role}-user.json
  fi
done

for es_path in "${!EVENT_ITEMS[@]}"; do
    endpoint="${EVENT_ITEMS[$es_path]}"
    if ! check_item "${es_path}"; then
        resource_file="${SCRIPTPATH}/event/${endpoint//_/-}.json"

        curl ${ES_AUTH} -X PUT "${ES_HOST}/${es_path}" \
            -H 'Content-Type: application/json' \
            -d @"${resource_file}"
    fi

    until check_item "${es_path}"; do
        sleep 2
    done
done

# Index template initialization commands
for template_name in "${!INDICES[@]}"; do
  resource_file="${SCRIPTPATH}/${INDICES[$template_name]}.json"
  es_path="_index_template/${template_name}"

  if ! check_item "$es_path"; then
    curl ${ES_AUTH} -X PUT "${ES_HOST}/${es_path}" \
      -H 'Content-Type: application/json' \
      -d "@${resource_file}"
  fi
done

# Index initialization commands
for index in "${INDICES_FOR_INSERTION[@]}"; do
  if ! check_item "${index}"; then
    curl ${ES_AUTH} -X PUT "${ES_HOST}/${index}?pretty"
  fi
done
