# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM ubuntu:22.04

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
        openjdk-17-jre-headless \
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
ADD --chown=evaka:evaka https://github.com/espoon-voltti/s3-downloader/releases/download/v1.3.0/s3downloader-linux-amd64 /usr/local/bin/s3download
RUN chmod u+x /usr/local/bin/s3download

ARG DD_JAVA_AGENT_VERSION="1.7.0"
ARG DD_JAVA_AGENT_SHA256="9c05cd65d927cd17ac4e298f3dfee941698828e7afd3cd11eed5a45039174e0b"

RUN curl -sSfo /opt/dd-java-agent.jar "https://repo1.maven.org/maven2/com/datadoghq/dd-java-agent/${DD_JAVA_AGENT_VERSION}/dd-java-agent-${DD_JAVA_AGENT_VERSION}.jar" \
 && echo "${DD_JAVA_AGENT_SHA256}  /opt/dd-java-agent.jar" | sha256sum -c -

ADD ./dd-jmxfetch/ /etc/jmxfetch/

USER evaka
WORKDIR /home/evaka

COPY --chown=evaka:evaka "entrypoint.sh" "entrypoint.sh"
COPY --chown=evaka:evaka "target/org/" "org/"
COPY --chown=evaka:evaka "target/META-INF" "META-INF/"
COPY --chown=evaka:evaka "target/BOOT-INF/lib" "BOOT-INF/lib/"
COPY --chown=evaka:evaka "target/BOOT-INF/classes" "BOOT-INF/classes/"

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
