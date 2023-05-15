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

upload_to_s3() {
    file_name=$1
    query=${2:-$file_name}

    filepath="$basepath/$file_name.csv"
    echo "Exporting $file_name data to $filepath"
    psql -c "COPY $query TO STDOUT WITH DELIMITER ',' CSV HEADER;" > "$filepath"
    zip -j "$basepath/${file_name}_$date.zip" "$filepath"
    aws s3 cp "$basepath/${file_name}_$date.zip" "s3://${S3_BUCKET}/${file_name}_$date.zip" "${s3_args[@]}"
}

table_names=$(psql -c "COPY (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type NOT IN ('VIEW') AND table_name NOT IN ('async_job', 'child_document', 'document_template', 'dvv_modification_token', 'employee_pin', 'flyway_schema_history', 'income_notification', 'mobile_device', 'mobile_device_push_subscription', 'scheduled_tasks', 'setting', 'varda_organizer_child', 'varda_reset_child', 'varda_service_need', 'varda_unit') ORDER BY table_name) TO STDOUT;")

for table_name in $table_names
do
    if [[ $include_schema = "true" ]]; then
        schemapath="$basepath/$table_name.sql"
        echo "Exporting $table_name schema to $schemapath"
        pg_dump --schema=public --no-owner --schema-only --table="$table_name" --no-acl > "$schemapath"
    fi

    upload_to_s3 "$table_name"
done

upload_to_s3 "absence_DELTA" "(SELECT * FROM absence WHERE modified_at >= (current_date AT TIME ZONE 'Europe/Helsinki' - interval '30 days')::date)"
upload_to_s3 "child_attendance_DELTA" "(SELECT * FROM child_attendance WHERE updated >= (current_date AT TIME ZONE 'Europe/Helsinki' - interval '30 days')::date)"
