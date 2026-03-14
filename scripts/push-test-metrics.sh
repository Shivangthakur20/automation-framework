#!/bin/bash

PASSED=1
FAILED=0

if [ "$BUILD_RESULT" != "SUCCESS" ]; then
FAILED=1
PASSED=0
fi

cat <<EOF | curl --data-binary @- $PUSHGATEWAY_URL/metrics/job/automation_tests

automation_tests_passed{module="$MODULE",env="$ENV",scope="$SUITE",browser="$BROWSER"} $PASSED
automation_tests_failed{module="$MODULE",env="$ENV",scope="$SUITE",browser="$BROWSER"} $FAILED

EOF