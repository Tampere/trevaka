# SPDX-FileCopyrightText: 2021-2022 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM ubuntu:22.04

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
    && apt-get -y install curl lsb-release gnupg \
    && curl https://www.postgresql.org/media/keys/ACCC4CF8.asc | gpg --dearmor | tee /etc/apt/trusted.gpg.d/apt.postgresql.org.gpg > /dev/null \
    && echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list \
    && apt-get update \
    && apt-get -y install postgresql-client-14 zip awscli \
    && rm -rf /var/lib/apt/lists/*
ADD db_to_csv.sh db_to_csv.sh
CMD ["./db_to_csv.sh"]
