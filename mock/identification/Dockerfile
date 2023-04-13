# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM eclipse-temurin:17-jammy AS builder
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./
RUN ./gradlew dependencies
COPY . .
RUN ./gradlew --offline build

FROM eclipse-temurin:17-jre-jammy
RUN mkdir -p /etc/cas/saml
COPY --from=builder build/libs/identification-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
