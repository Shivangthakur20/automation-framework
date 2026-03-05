#!/bin/bash

echo "Waiting for Selenium Grid..."

GRID_URL="http://selenium-hub:4444/status"

until curl -s $GRID_URL | grep '"ready": true' > /dev/null; do
  echo "Grid not ready..."
  sleep 2
done

echo "Grid ready. Running tests..."

mvn clean test -Drun.mode=remote