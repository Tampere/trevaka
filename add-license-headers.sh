#!/usr/bin/env bash

# SPDX-FileCopyrightText: 2017-2020 City of Espoo
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -euo pipefail

# Configuration
DEBUG=${DEBUG:-false}
REUSE_VERSION=5.0.2 # NOTE: Update .circleci/config.yml to match
START_YEAR=2023
CURRENT_YEAR=$(date +"%Y")
if [ "${START_YEAR}" == "${CURRENT_YEAR}" ]; then
    REUSE_YEARS="${CURRENT_YEAR}"
else
    REUSE_YEARS="${START_YEAR}-${CURRENT_YEAR}"
fi

REUSE_IMAGE="fsfe/reuse:${REUSE_VERSION}"

if [ "$DEBUG" = "true" ]; then
    set -x
fi

# Figure out absolute path to git repository root
REPO_ROOT=$(git rev-parse --show-superproject-working-tree) # first, let's assume the working directory is in a git submodule
if [ -z "${REPO_ROOT}" ]; then
  # not in a submodule -> just get the repository root
  REPO_ROOT=$(git rev-parse --show-toplevel)
fi

# strip absolute git repository path from absolute working directory path
REPO_PREFIX=${PWD#"${REPO_ROOT}"}

if [ "${1:-X}" = '--help' ]; then
  echo 'Usage: ./bin/add-license-headers.sh [OPTIONS]'
  echo ''
  echo 'Helper script to attempt automatically adding missing license headers to all source code files.'
  echo 'Any missing license files are downloaded automatically to LICENSES/.'
  echo ''
  echo 'Options:'
  echo "    --lint-only     Only lint for missing headers, don't attempt to add anything"
  echo '    --help          Print this help'
  exit 0
fi

function run_reuse() {
    run_args=("$@")
    docker run -u "${UID}" --rm --volume "${REPO_ROOT}:/data" --workdir "/data${REPO_PREFIX}" "$REUSE_IMAGE" "${run_args[@]}"
}

function addheader() {
    local file="$1"
    run_reuse annotate --license "LGPL-2.1-or-later" --copyright "Tampere region" --year "$REUSE_YEARS" "$file"
}

# MAIN SCRIPT

set +e
REUSE_OUTPUT=$(run_reuse lint)
REUSE_EXIT_CODE="$?"
set -e

# No need to continue if everything was OK, or we are just linting
if [ "$REUSE_EXIT_CODE" = 0 ] || [ "${1:-X}" = "--lint-only" ]; then
    echo "$REUSE_OUTPUT"
    exit "$REUSE_EXIT_CODE"
fi

# Linter failures use exit code 1, everything else is unexpected
if [ "$REUSE_EXIT_CODE" != 1 ]; then
    >&2 echo 'ERROR: Unexpected failure during reuse tool execution. Exiting with original exit code!'
    echo "$REUSE_OUTPUT"
    exit "$REUSE_EXIT_CODE"
fi

set +e
REUSE_OUTPUT_JSON=$(run_reuse lint --json)
set -e

# shellcheck disable=SC2207
MISSING_LICENSES=($(echo "$REUSE_OUTPUT_JSON" | jq -r '.non_compliant.missing_licenses | keys | .[]'))
echo "${MISSING_LICENSES[@]}"
for license in "${MISSING_LICENSES[@]}"; do
    if [ -z "$license" ]; then
        continue
    fi

    if [ ! -f "${REPO_ROOT}/LICENSES/${license}.txt" ]; then
        run_reuse download "$license"
    fi
done

NONCOMPLIANT_FILES=$(echo "$REUSE_OUTPUT_JSON" \
    | jq -r '.non_compliant.missing_copyright_info + .non_compliant.missing_licensing_info | unique | .[]')

while IFS= read -r file; do
    if [ -z "$file" ]; then
        continue
    fi

    addheader "$file"
done <<< "$NONCOMPLIANT_FILES"

echo 'All files are REUSE compliant'
