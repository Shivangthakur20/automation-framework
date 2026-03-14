#!/bin/bash

RESULT_FILE=$(find . -name "testng-results.xml" | head -1)

PASSED=$(grep -o 'passed="[0-9]*"' $RESULT_FILE | cut -d'"' -f2)
FAILED=$(grep -o 'failed="[0-9]*"' $RESULT_FILE | cut -d'"' -f2)
SKIPPED=$(grep -o 'skipped="[0-9]*"' $RESULT_FILE | cut -d'"' -f2)

TOTAL=$((PASSED + FAILED + SKIPPED))

MODULE=${MODULE}
ENV=${ENV}
SUITE=${SUITE}

cat <<EOF | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/${MODULE}_tests
automation_tests_total{module="${MODULE}",env="${ENV}",suite="${SUITE}"} ${TOTAL}
automation_tests_passed{module="${MODULE}",env="${ENV}",suite="${SUITE}"} ${PASSED}
automation_tests_failed{module="${MODULE}",env="${ENV}",suite="${SUITE}"} ${FAILED}
automation_tests_skipped{module="${MODULE}",env="${ENV}",suite="${SUITE}"} ${SKIPPED}
EOF
