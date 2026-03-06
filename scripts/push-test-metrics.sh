#!/usr/bin/env bash
# Push test-run metrics to Prometheus Pushgateway.
# Labels: job=automation, env, module, suite.
# Usage: ENV=dev MODULE=web-ui SUITE=smoke [PUSHGATEWAY_URL=http://host:9091] [BUILD_RESULT=SUCCESS] ./scripts/push-test-metrics.sh
# Run from workspace root (where web-ui/ and api/ exist).

set -e

ENV="${ENV:-dev}"
MODULE="${MODULE:-}"
SUITE="${SUITE:-full}"
PUSHGATEWAY_URL="${PUSHGATEWAY_URL:-http://localhost:9091}"
BUILD_RESULT="${BUILD_RESULT:-UNKNOWN}"

if [ -z "$MODULE" ]; then
  echo "MODULE not set (e.g. web-ui, api). Skipping push."
  exit 0
fi

REPORT_DIR="${MODULE}/target/surefire-reports"
if [ ! -d "$REPORT_DIR" ]; then
  echo "No surefire reports at $REPORT_DIR. Skipping push."
  exit 0
fi

total=0
failures=0
errors=0
skipped=0
duration_sum=0

for f in "${REPORT_DIR}"/TEST-*.xml; do
  [ -f "$f" ] || continue
  t=$(sed -n 's/.*tests="\([0-9]*\)".*/\1/p' "$f"); total=$((total + ${t:-0}))
  fa=$(sed -n 's/.*failures="\([0-9]*\)".*/\1/p' "$f"); failures=$((failures + ${fa:-0}))
  e=$(sed -n 's/.*errors="\([0-9]*\)".*/\1/p' "$f"); errors=$((errors + ${e:-0}))
  s=$(sed -n 's/.*skipped="\([0-9]*\)".*/\1/p' "$f"); skipped=$((skipped + ${s:-0}))
  d=$(sed -n 's/.*time="\([0-9.]*\)".*/\1/p' "$f"); duration_sum=$(awk "BEGIN { print $duration_sum + ${d:-0} }")
done

duration_sum=${duration_sum:-0}
passed=$((total - failures - errors - skipped))
[ "$passed" -lt 0 ] && passed=0

# BUILD_RESULT: SUCCESS=1, else 0
build_success=0
case "$BUILD_RESULT" in
  SUCCESS) build_success=1 ;;
  UNSTABLE) build_success=1 ;;
  *) build_success=0 ;;
esac

# Pushgateway: labels in URL path
# Metric names must match Prometheus naming (snake_case, suffix _total for counters, _seconds for seconds)
url="${PUSHGATEWAY_URL}/metrics/job/automation/env/${ENV}/module/${MODULE}/suite/${SUITE}"

body="automation_tests_total ${total}
automation_tests_passed ${passed}
automation_tests_failed ${failures}
automation_tests_errors ${errors}
automation_tests_skipped ${skipped}
automation_run_duration_seconds ${duration_sum}
automation_build_success ${build_success}"

if echo "$body" | curl -s --data-binary @- "$url"; then
  echo "Pushed metrics to Pushgateway: env=$ENV module=$MODULE suite=$SUITE (tests=$total passed=$passed failed=$failures duration=${duration_sum}s)"
else
  echo "Push to Pushgateway failed (url=$url). Is Pushgateway reachable?"
  exit 0
fi
