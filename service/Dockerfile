# syntax=docker/dockerfile:1.6.0
# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM eclipse-temurin:21-jammy AS builder
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

FROM eclipse-temurin:21-jre-jammy

LABEL maintainer="https://github.com/tampere/trevaka"

ARG CACHE_BUST

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
    --checksum=sha256:520ea232e83a7cefe2a87d4f2af8433e383a4351464e213b7dd3b78ca0dc200f \
    https://github.com/espoon-voltti/s3-downloader/releases/download/v1.4.1/s3downloader-linux-amd64 \
    /usr/local/bin/s3download

ADD --chmod=0644 \
    --checksum=sha256:776b76cfe05611cdc3d38c4b35fc92f563fcfd6e3df0e28c524da1d279d44223 \
    https://github.com/DataDog/dd-trace-java/releases/download/v1.31.0/dd-java-agent.jar \
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
