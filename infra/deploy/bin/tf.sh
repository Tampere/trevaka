#!/bin/bash

# SPDX-FileCopyrightText: 2026 City of Tampere
# SPDX-License-Identifier: LGPL-2.1-or-later

# usage: bin/tf.sh <environment> <terraform subcommand> [args...]
# where <environment> is tampere-test, hameenkyro-prod etc
set -xueo pipefail

ENVIRONMENT="$1"
ENVIRONMENT_TAG="${ENVIRONMENT##*-}"  # tampere-test -> test
shift

AWS_PROFILE="trevaka-${ENVIRONMENT_TAG}" # tampere-test -> trevaka-test
export AWS_PROFILE

TERRAFORM_BUCKET="trevaka-${ENVIRONMENT_TAG}-terraform"

SUBCOMMAND="$1"
shift

if [ "$SUBCOMMAND" = "init" ]; then
  exec terraform "$SUBCOMMAND" \
    -backend-config="bucket=${TERRAFORM_BUCKET}" \
    -backend-config="region=eu-west-1" \
    -reconfigure \
    "$@"
else
  exec terraform "$SUBCOMMAND" \
    -var-file "./variables/${ENVIRONMENT}-apps.tfvars" \
    -var region=eu-west-1 \
    -var terraform_bucket="${TERRAFORM_BUCKET}" \
    -var utility_account_id=518207624130 \
    "$@"
fi
