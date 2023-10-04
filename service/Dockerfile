# syntax=docker/dockerfile:1.6.0
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM eclipse-temurin:17-jammy AS builder
WORKDIR /evaka
COPY --from=core ./service/gradle/ ./service/gradle/
COPY --from=core ./service/build.gradle.kts ./service/gradle.properties ./service/settings.gradle.kts ./service/
COPY --from=core ./service/vtjclient/build.gradle.kts ./service/vtjclient/build.gradle.kts
COPY --from=core ./service/service-lib/*.kts ./service/service-lib/
COPY --from=core ./service/evaka-bom/*.kts ./service/evaka-bom/
COPY --from=core ./service/sficlient/*.kts ./service/sficlient/
WORKDIR /service
COPY ./gradle/ ./gradle/
COPY ./gradlew ./build.gradle.kts ./gradle.properties ./settings.gradle.kts ./settings-dependabot-ignore.gradle.kts ./
RUN ./gradlew --no-daemon resolveDependencies
COPY --from=core ./service/sficlient/ ../evaka/service/sficlient/
COPY --from=core ./service/vtjclient/ ../evaka/service/vtjclient/
RUN ./gradlew --no-daemon :service:sficlient:wsdl2java :service:vtjclient:wsdl2java
COPY --from=core . ../evaka
COPY . ./
RUN ./gradlew --offline --no-daemon assemble \
    && java -Djarmode=layertools -jar build/libs/trevaka-service.jar extract --destination build/extracted

FROM eclipse-temurin:17-jre-jammy

LABEL maintainer="https://github.com/tampere/trevaka"

# Increment this if we ever explicitly want to e.g. run apt-get upgrade
ARG CACHE_BUST=2022-11-08

# Use bash instead of dash
SHELL ["/bin/bash", "-c"]

# Install required packages and update time zone information
ENV LC_ALL C.UTF-8
ENV LANG C.UTF-8
ENV LANGUAGE C.UTF-8
RUN set -euxo pipefail \
    && apt-get update \
    && apt-get -y dist-upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        tzdata \
        ca-certificates \
        curl \
        jq \
    && ln -fs /usr/share/zoneinfo/Europe/Helsinki /etc/localtime \
    && dpkg-reconfigure --frontend noninteractive tzdata \
    && rm -rf /var/lib/apt/lists/*

# Set evaka username
ENV USERNAME evaka
ENV HOME_DIR /home/${USERNAME}
ENV USER_ID 1000

# Create a new user named evaka which should be used to run any software.
RUN adduser ${USERNAME} --gecos "" -q --home ${HOME_DIR} --uid ${USER_ID} --disabled-password

# Used for downloading keys from S3
ADD --chown=evaka:evaka --chmod=0700 \
    --checksum=sha256:d0ee074cbc04c1a36fb8cee6f99d9ff591fee89ea38d34a328d0ee1acb039a48 \
    https://github.com/espoon-voltti/s3-downloader/releases/download/v1.3.0/s3downloader-linux-amd64 \
    /usr/local/bin/s3download

ADD --chmod=0644 \
    --checksum=sha256:208e18c86185443e53ed9d10f6b87943433860bf1589c1fddb51fa2ceea405fb \
    https://github.com/DataDog/dd-trace-java/releases/download/v1.21.0/dd-java-agent-1.21.0.jar \
    /opt/dd-java-agent.jar

ADD ./dd-jmxfetch/ /etc/jmxfetch/

USER evaka
WORKDIR /home/evaka

COPY --chown=evaka:evaka "entrypoint.sh" "entrypoint.sh"
COPY --from=builder --chown=evaka:evaka /service/build/extracted/dependencies/ ./
COPY --from=builder --chown=evaka:evaka /service/build/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=evaka:evaka /service/build/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=evaka:evaka /service/build/extracted/application/ ./

# Add build and commit environment variables and labels
# for tracing the image to the commit and build from which the image has been built.
ARG build=none
ARG commit=none
ENV APP_BUILD "$build"
ENV APP_COMMIT "$commit"
LABEL fi.tampere.build="$build" \
      fi.tampere.commit="$commit"

EXPOSE 8888

ENTRYPOINT ["./entrypoint.sh"]
