# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM node:16.13 AS theme
COPY theme/package*.json ./
RUN npm ci
COPY theme .
RUN npm run build

FROM quay.io/keycloak/keycloak:14.0.0
COPY --from=theme evaka /opt/jboss/keycloak/themes/evaka
