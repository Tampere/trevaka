# syntax=docker/dockerfile:1.6.0
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM node:20.11.0 AS theme
COPY --from=core theme/package*.json ./
RUN npm ci
COPY --from=core theme .
RUN sed -i -e '/<#--/d' -e '/-->/d' evaka/login/login-update-profile.ftl

FROM theme AS tampere
COPY themes/tampere/theme-fi.sed ./
COPY themes/tampere/TampereLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM theme AS vesilahti
COPY themes/vesilahti/theme-fi.sed ./
COPY themes/vesilahti/VesilahtiLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM theme AS hameenkyro
COPY themes/hameenkyro/theme-fi.sed ./
COPY themes/hameenkyro/HameenkyroLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM quay.io/keycloak/keycloak:23.0.5
ADD --chmod=0644 \
    --checksum=sha256:424a3e03a17df0a2bc2b3ca749d81b04e79d59cb7aeec8876a5a3f308d0caf51 \
    https://raw.githubusercontent.com/danielmiessler/SecLists/2023.1/Passwords/xato-net-10-million-passwords-1000000.txt \
    /opt/keycloak/data/password-blacklists/default.txt
COPY --from=tampere evaka /opt/keycloak/themes/evaka-tampere
COPY --from=vesilahti evaka /opt/keycloak/themes/evaka-vesilahti
COPY --from=hameenkyro evaka /opt/keycloak/themes/evaka-hameenkyro
RUN /opt/keycloak/bin/kc.sh build --db=postgres --http-relative-path=/auth --health-enabled=true
RUN /opt/keycloak/bin/kc.sh show-config
CMD ["start","--optimized"]
