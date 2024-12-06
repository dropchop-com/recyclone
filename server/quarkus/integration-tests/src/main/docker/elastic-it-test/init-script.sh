#!/bin/bash

/init-index.sh &

exec env "$@"
