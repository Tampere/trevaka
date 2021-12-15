FROM openjdk:17-alpine AS builder
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./
RUN ./gradlew build
COPY . .
RUN ./gradlew build

FROM openjdk:17-alpine
RUN mkdir -p /etc/cas/saml
COPY --from=builder build/libs/identification-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
