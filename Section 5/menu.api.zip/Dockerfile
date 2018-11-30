FROM openjdk:8-jdk-alpine AS base
WORKDIR /app
EXPOSE 8080

FROM maven:3.5.4-jdk-8-alpine AS build
ARG APP_VERSION
WORKDIR /app
COPY . .
RUN mvn versions:set -DnewVersion=${APP_VERSION}
RUN mvn clean package

FROM base AS final
ARG APP_VERSION
WORKDIR /app
COPY --from=build /app/target/menu.api-${APP_VERSION}.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.edg=file:/dev/./urandom","-jar","./app.jar"]
