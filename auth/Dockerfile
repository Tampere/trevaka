# syntax=docker/dockerfile:1.6.0
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM node:20.13.1 AS theme
COPY --from=core theme/package*.json ./
RUN npm ci
COPY --from=core theme .
RUN rm evaka/login/idp-review-user-profile.ftl

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

FROM theme AS kangasala
COPY themes/kangasala/theme-fi.sed ./
COPY themes/kangasala/KangasalaLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM theme AS lempaala
COPY themes/lempaala/theme-fi.sed ./
COPY themes/lempaala/LempaalaLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM theme AS nokia
COPY themes/nokia/theme-fi.sed ./
COPY themes/nokia/NokiaLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM theme AS pirkkala
COPY themes/pirkkala/theme-fi.sed ./
COPY themes/pirkkala/PirkkalaLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM theme AS ylojarvi
COPY themes/ylojarvi/theme-fi.sed ./
COPY themes/ylojarvi/YlojarviLogo.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties
RUN npm run build && rm -r evaka/account

FROM quay.io/keycloak/keycloak:24.0.4
ADD --chmod=0644 \
    --checksum=sha256:424a3e03a17df0a2bc2b3ca749d81b04e79d59cb7aeec8876a5a3f308d0caf51 \
    https://raw.githubusercontent.com/danielmiessler/SecLists/2023.1/Passwords/xato-net-10-million-passwords-1000000.txt \
    /opt/keycloak/data/password-blacklists/default.txt
COPY --from=tampere evaka /opt/keycloak/themes/evaka-tampere
COPY --from=vesilahti evaka /opt/keycloak/themes/evaka-vesilahti
COPY --from=hameenkyro evaka /opt/keycloak/themes/evaka-hameenkyro
COPY --from=kangasala evaka /opt/keycloak/themes/evaka-kangasala
COPY --from=lempaala evaka /opt/keycloak/themes/evaka-lempaala
COPY --from=nokia evaka /opt/keycloak/themes/evaka-nokia
COPY --from=pirkkala evaka /opt/keycloak/themes/evaka-pirkkala
COPY --from=ylojarvi evaka /opt/keycloak/themes/evaka-ylojarvi
RUN /opt/keycloak/bin/kc.sh build --cache=ispn --cache-stack=kubernetes --db=postgres --http-relative-path=/auth --health-enabled=true
RUN /opt/keycloak/bin/kc.sh show-config
CMD ["start","--optimized"]
