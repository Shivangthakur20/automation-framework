#!/bin/bash

echo "--------------------------------------"
echo "Pushing automation metrics..."
echo "--------------------------------------"

PUSHGATEWAY=${PUSHGATEWAY_URL:-http://localhost:9091}

MODULE=${MODULE:-unknown}
ENV=${ENV:-unknown}
SCOPE=${SUITE:-full}
BROWSER=${BROWSER:-na}

PASSED=${TESTS_PASSED:-0}
FAILED=${TESTS_FAILED:-0}
SKIPPED=${TESTS_SKIPPED:-0}
DURATION=${EXECUTION_TIME:-0}

echo "Module: $MODULE"
echo "Environment: $ENV"
echo "Scope: $SCOPE"
echo "Browser: $BROWSER"

echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "Skipped: $SKIPPED"
echo "Duration: $DURATION"

cat <<EOF | curl --data-binary @- $PUSHGATEWAY/metrics/job/automation_tests

automation_tests_passed{module="$MODULE",env="$ENV",scope="$SCOPE",browser="$BROWSER"} $PASSED
automation_tests_failed{module="$MODULE",env="$ENV",scope="$SCOPE",browser="$BROWSER"} $FAILED
automation_tests_skipped{module="$MODULE",env="$ENV",scope="$SCOPE",browser="$BROWSER"} $SKIPPED
automation_tests_duration_seconds{module="$MODULE",env="$ENV",scope="$SCOPE"} $DURATION

EOF

echo "--------------------------------------"
echo "Metrics pushed successfully"
echo "--------------------------------------"