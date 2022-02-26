#!/bin/bash

./scripts/shutdown-containers.sh
docker-compose up -d
docker ps -a
