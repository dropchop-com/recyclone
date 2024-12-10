#!/bin/bash

# Absolute path to this script
SCRIPT=$(readlink -f $0)
# Directory of an absolute path to this script
SCRIPTPATH=`dirname $SCRIPT`
ES_HOST=http://localhost:9200
INDICES=("dummy" "event")
ROLES_USERS=( )

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

# Index template initialization commands
for index in "${INDICES[@]}"; do
  if ! check_item "_index_template/${index}_index_tpl"; then
    curl ${ES_AUTH} -X PUT "${ES_HOST}/_index_template/${index}_index_tpl" -H 'Content-Type: application/json' \
      -d @${SCRIPTPATH}/${index}-index-tpl.json
  fi
done

# Index initialization commands
for index in "${INDICES[@]}"; do
  if ! check_item "${index}"; then
    curl ${ES_AUTH} -X PUT "${ES_HOST}/${index}?pretty"
  fi
done
