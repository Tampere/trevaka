# syntax=docker/dockerfile:1.15.1
# check=error=true
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM eclipse-temurin:25-noble AS builder
WORKDIR /service
COPY ./gradle/ ./gradle/
COPY ./gradlew ./
RUN ./gradlew --version
COPY --from=core . ../evaka
COPY . ./
RUN ./gradlew assemble \
    && java -Djarmode=tools -jar build/libs/trevaka-service.jar extract --layers --launcher --destination build/extracted

FROM eclipse-temurin:25-jre-noble
LABEL maintainer="https://github.com/tampere/trevaka"
ENV LC_ALL=C.UTF-8
ENV LANG=C.UTF-8
ENV LANGUAGE=C.UTF-8
ADD --chmod=0755 \
    --checksum=sha256:520ea232e83a7cefe2a87d4f2af8433e383a4351464e213b7dd3b78ca0dc200f \
    https://github.com/espoon-voltti/s3-downloader/releases/download/v1.4.1/s3downloader-linux-amd64 \
    /usr/local/bin/s3download
ADD --chmod=u=rwX,go=rX \
    --checksum=sha256:424a3e03a17df0a2bc2b3ca749d81b04e79d59cb7aeec8876a5a3f308d0caf51 \
    https://raw.githubusercontent.com/danielmiessler/SecLists/2023.1/Passwords/xato-net-10-million-passwords-1000000.txt \
    /opt/password-blacklists/xato-net-10-million-passwords-1000000.txt

ARG CACHE_BUST
RUN apt-get update \
    && apt-get -y dist-upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        tzdata \
        ca-certificates \
        curl \
        jq \
    && ln -fs /usr/share/zoneinfo/Europe/Helsinki /etc/localtime \
    && dpkg-reconfigure --frontend noninteractive tzdata \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /evaka/service
COPY entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
COPY --from=builder /service/build/extracted/dependencies/ ./
COPY --from=builder /service/build/extracted/spring-boot-loader/ ./
COPY --from=builder /service/build/extracted/snapshot-dependencies/ ./
COPY --from=builder /service/build/extracted/application/ ./

USER ubuntu

ARG build=none
ARG commit=none
ENV APP_BUILD="$build"
ENV APP_COMMIT="$commit"
LABEL fi.tampere.build="$build" \
      fi.tampere.commit="$commit"
