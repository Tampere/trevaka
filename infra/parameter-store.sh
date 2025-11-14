#!/bin/bash

# SPDX-FileCopyrightText: 2023-2025 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -euo pipefail

municipalities=(
  "tampere"
  "vesilahti"
  "hameenkyro"
  "nokia"
  "ylojarvi"
  "pirkkala"
  "kangasala"
  "lempaala"
  "orivesi"
)
environments=(
  "dev"
  "test"
  "prod"
)
parameters=(
  "/service/vtj/kysely/api-key"
  "/service/vtj/mutpa/api-key"
  "/service/frends/api-key"
  "/service/invoice/url/frends"
  "/service/payment/url/frends"
  "/service/frends/sftp/host"
  "/service/frends/sftp/port"
  "/service/frends/sftp/host-keys"
  "/service/frends/sftp/username"
  # "/service/frends/sftp/private-key"
)

if [ "$#" -ne 3 ]; then
  echo "Usage: $0 <municipality> <environment> <parameter>"
  exit 1
fi

municipality=$1
environment=$2
parameter=$3

if [[ ! ("${municipalities[*]}" =~ $municipality) ]]; then
  echo "Supported municipalities: ${municipalities[*]}"
  exit 1
fi
if [[ ! ("${environments[*]}" =~ $environment) ]]; then
  echo "Supported environments: ${environments[*]}"
  exit 1
fi
if [[ ! ("${parameters[*]}" =~ $parameter) ]]; then
  echo "Supported parameters: ${parameters[*]}"
  exit 1
fi

if [[ $municipality = "tampere" ]]; then
  prefix="/trevaka-$environment"
else
  prefix="/$municipality-evaka-$environment"
fi

name="$prefix$parameter"
echo "Name: $name"

set +e
response=$(aws ssm get-parameter --with-decryption --name "$name" --query 'Parameter.Value' 2>&1)
code=$?
set -e
if [[ $code = "0" ]]; then
  echo "Current value: $response"
  read -r -p "Overwrite? (y/n): " overwrite
  if [[ $overwrite = "y" ]]; then
    read -r -p "New value: " value
    aws ssm put-parameter \
      --type "SecureString" \
      --key-id "alias/$municipality-evaka-$environment-encryption" \
      --name "$name" \
      --value "$value" \
      --overwrite
  fi
elif [[ $code = "254" && $response =~ (.*)ParameterNotFound(.*) ]]; then
  echo "Current value: (not found)"
  read -r -p "Create? (y/n): " create
  if [[ $create = "y" ]]; then
    read -r -p "New value: " value
    aws ssm put-parameter \
      --type "SecureString" \
      --key-id "alias/$municipality-evaka-$environment-encryption" \
      --name "$name" \
      --value "$value" \
      --tags "Key=Municipality,Value=$municipality"
  fi
else
  echo "$response"
  exit $code
fi
