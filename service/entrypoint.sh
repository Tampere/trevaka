#!/usr/bin/env bash

# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -euo pipefail

# For log tagging
# shellcheck disable=SC2155
if [ -z "${ECS_CONTAINER_METADATA_URI_V4+x}" ]; then
  export HOST_IP="UNAVAILABLE"
else
  export HOST_IP=$(curl --silent --fail --show-error ${ECS_CONTAINER_METADATA_URI_V4}/task | jq -r '.AvailabilityZone')
fi

# Download deployment specific files from S3 if in a non-local environment
if [ "${VOLTTI_ENV:-X}" != "local" ]; then
  s3download "$DEPLOYMENT_BUCKET" evaka-srv /home/ubuntu/s3
fi

# Run as exec so the application can receive any Unix signals sent to the container, e.g.,
# Ctrl + C.
# shellcheck disable=SC2086
exec java -cp . -server $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher "$@"
