# syntax=docker/dockerfile:1.5.2-labs
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM node:18.16 AS theme
COPY --from=core theme/package*.json ./
RUN npm ci
COPY --from=core theme .
COPY theme-fi.sed ./
COPY TampereLogo.png evaka/login/resources/img/EspooLogo.png
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties && \
    sed -i -e '/<#--/d' -e '/-->/d' evaka/login/login-update-profile.ftl
RUN npm run build

FROM quay.io/keycloak/keycloak:21.1.1
COPY --from=theme evaka /opt/keycloak/themes/evaka
RUN /opt/keycloak/bin/kc.sh build --db=postgres --http-relative-path=/auth --health-enabled=true
RUN /opt/keycloak/bin/kc.sh show-config
CMD ["start"]
