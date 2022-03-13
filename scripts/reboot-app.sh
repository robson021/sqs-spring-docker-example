#!/bin/bash

killall -9 java
./scripts/restart-queue.sh
./gradlew bootRun
