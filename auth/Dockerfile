# syntax=docker/dockerfile:1.5.2-labs
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM node:18.16.1 AS theme
COPY --from=core theme/package*.json ./
RUN npm ci
COPY --from=core theme .
COPY theme-fi.sed ./
COPY TampereLogo.png evaka/login/resources/img/EspooLogo.png
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties && \
    sed -i -e '/<#--/d' -e '/-->/d' evaka/login/login-update-profile.ftl
RUN npm run build

FROM quay.io/keycloak/keycloak:22.0.0
ADD --chmod=0644 \
    --checksum=sha256:424a3e03a17df0a2bc2b3ca749d81b04e79d59cb7aeec8876a5a3f308d0caf51 \
    https://raw.githubusercontent.com/danielmiessler/SecLists/2023.1/Passwords/xato-net-10-million-passwords-1000000.txt \
    /opt/keycloak/data/password-blacklists/default.txt
COPY --from=theme evaka /opt/keycloak/themes/evaka
RUN /opt/keycloak/bin/kc.sh build --db=postgres --http-relative-path=/auth --health-enabled=true
RUN /opt/keycloak/bin/kc.sh show-config
CMD ["start","--optimized"]
