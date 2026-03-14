#!/bin/bash

PASSED=1
FAILED=0

cat <<EOF | curl --data-binary @- $PUSHGATEWAY_URL/metrics/job/automation
automation_tests_passed{module="$MODULE",env="$ENV",suite="$SUITE"} $PASSED
automation_tests_failed{module="$MODULE",env="$ENV",suite="$SUITE"} $FAILED
EOF