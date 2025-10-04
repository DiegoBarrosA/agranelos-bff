# syntax=docker/dockerfile:1

# Etapa 1: Build con Maven
FROM docker.io/library/maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final minimalista
FROM docker.io/library/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/agranelos-bff-*.jar app.jar

ENV JAVA_OPTS=""
ENV AZURE_FUNCTIONS_BASE_URL=""

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
