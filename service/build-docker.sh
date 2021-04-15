#!/usr/bin/env bash

# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -euo pipefail

DOCKER_IMAGE=${DOCKER_IMAGE:-trevaka/service}
DOCKER_TAG=${DOCKER_TAG:-local}
GIT_SHA=$(git rev-parse HEAD)

rm -rf target
./gradlew assemble
unzip -oq build/libs/trevaka-service.jar -d target
mkdir target/buildInfo

docker build --build-arg commit="$GIT_SHA" --build-arg build=local -t "${DOCKER_IMAGE}" .
docker tag "${DOCKER_IMAGE}" "${DOCKER_IMAGE}:${DOCKER_TAG}"
docker tag "${DOCKER_IMAGE}:${DOCKER_TAG}" "${DOCKER_IMAGE}:${GIT_SHA}"
