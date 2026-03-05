#!/bin/bash

echo "Waiting for Selenium Grid to be ready..."

GRID_URL="http://selenium-hub:4444/status"

until curl -s $GRID_URL | grep '"ready": true' > /dev/null; do
  echo "Grid not ready yet. Waiting..."
  sleep 2
done

echo "Selenium Grid is ready. Starting tests..."

mvn clean test -Drun.mode=remote -Dheadless=true