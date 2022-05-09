#!/bin/bash

# SPDX-FileCopyrightText: 2017-2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later
#
# Scavenged from evaka integration testing and e2e compose files

set -euo pipefail

export DEBUG="${DEBUG:-false}"
export CI="${CI:-false}"
export FORCE_COLOR=1
export PROXY_URL=${PROXY_URL:-http://evaka-proxy:8080}

if [ "${DEBUG}" = "true" ]; then
    set -x
fi

export FORCE_COLOR=1

cd /repo/evaka/frontend

echo 'INFO: Waiting for compose stack to be up...'
./wait-for-url.sh "${PROXY_URL}/api/internal/dev-api"

cd /repo/frontend

yarn e2e-ci-playwright
