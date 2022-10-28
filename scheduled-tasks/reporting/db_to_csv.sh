#!/bin/bash

# SPDX-FileCopyrightText: 2021-2022 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# usage:
# Use environment variables to configure connection: https://www.postgresql.org/docs/13/libpq-envars.html
#
# example:
# PGHOST=postgres.example.org PGUSER=postgres PGPASSWORD=postgres PGDATABASE=database_name_here ./db_to_csv.sh

set -e

if [[ -z "${PGHOST}" || -z "${PGUSER}" || -z "${PGPASSWORD}" || -z "${PGDATABASE}" || -z "${S3_BUCKET}" ]]; then
    echo "ERROR: Required environment variables are not set"
    exit 1
fi

s3_args=()
if [[ -n "${S3_ENDPOINT_URL}" ]]; then
    s3_args+=("--endpoint-url ${S3_ENDPOINT_URL}")
fi

include_schema=${INCLUDE_SCHEMA:-false}

date=$(date --iso-8601)
basepath="export/$date"
mkdir -p "$basepath"

table_names=$(psql -c "COPY (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type NOT IN ('VIEW') AND table_name NOT IN ('async_job', 'dvv_modification_token', 'employee', 'employee_pin', 'evaka_user', 'flyway_schema_history', 'mobile_device', 'old_fee_decision', 'old_fee_decision_part', 'old_service_need', 'scheduled_tasks', 'setting', 'unit_manager', 'varda_organizer_child', 'varda_reset_child', 'varda_service_need', 'varda_unit') ORDER BY table_name) TO STDOUT;")

for table_name in $table_names
do
    if [[ $include_schema = "true" ]]; then
        schemapath="$basepath/$table_name.sql"
        echo "Exporting $table_name schema to $schemapath"
        pg_dump --schema=public --no-owner --schema-only --table="$table_name" --no-acl > "$schemapath"
    fi

    filepath="$basepath/$table_name.csv"
    echo "Exporting $table_name data to $filepath"
    psql -c "COPY $table_name TO STDOUT WITH DELIMITER ',' CSV HEADER;" > "$filepath"

    zip -j "$basepath/${table_name}_$date.zip" "$filepath"
    aws s3 cp "$basepath/${table_name}_$date.zip" "s3://${S3_BUCKET}/${table_name}_$date.zip" "${s3_args[@]}"
done
