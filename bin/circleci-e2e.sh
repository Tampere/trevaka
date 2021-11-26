#!/bin/bash

# SPDX-FileCopyrightText: 2017-2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later
#
# Scavenged from evaka integration testig and e2e compose files

# Run E2E tests inside a container in CircleCI with the app running in compose.
#
# Usage:
#   1. Start compose stack
#   2. Create split test split file under frontend/ for target test runner
#   3. Run this script



set -euo pipefail

if [ "${DEBUG:-X}" = "true" ]; then
  set -x
fi

COMPOSE_NETWORK=${COMPOSE_NETWORK:-compose_default}
SKIP_SPLIT=${SKIP_SPLIT:-false}
PLAYWRIGHT_VERSION=${PLAYWRIGHT_VERSION:-v1.16.2}

# Ensure we are in repository root
pushd "$(dirname "${BASH_SOURCE[0]}")"/..

if [ "$SKIP_SPLIT" != 'true' ]; then
  pushd frontend
  # Get list of test files that should run on this node.
  mapfile -t FILENAMES < <(circleci tests glob \
      'src/e2e-playwright/specs/**/*.spec.ts' \
      | sort -h \
      | circleci tests split --split-by=timings --timings-type=filename)
  printf '%s\n' 'Spec files selected for node:' "${FILENAMES[@]}"
  printf "%s\n" "${FILENAMES[@]}" > playwright-filenames.txt
  popd
fi

pushd compose

./test-e2e
