#!/bin/sh

# SPDX-FileCopyrightText: 2021-2023 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -e

if [ -z "${CLOUDWATCH_LOG_GROUP_NAME}" ] || [ -z "${S3_BUCKET}" ]; then
    echo "ERROR: Required environment variables are not set"
    exit 1
fi

prefix=$(date --utc --date "yesterday" +%Y/%m/%d)
from=$(date --utc --date "yesterday 0" +%s000)
to=$(date --utc --date "today 0 -1second" +%s999)

aws logs create-export-task \
    --task-name "$prefix/$CLOUDWATCH_LOG_GROUP_NAME" \
    --log-group-name "$CLOUDWATCH_LOG_GROUP_NAME" \
    --from "$from" \
    --to "$to" \
    --destination "$S3_BUCKET" \
    --destination-prefix "$prefix/$CLOUDWATCH_LOG_GROUP_NAME"
