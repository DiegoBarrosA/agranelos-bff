# syntax=docker/dockerfile:1

# Multi-stage build for Agranelos BFF
# Stage 1: Maven build environment
FROM docker.io/library/maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy dependency configuration for layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production runtime
FROM docker.io/library/eclipse-temurin:17-jre-alpine

# Create non-root user for security
RUN addgroup -g 1000 appgroup && \
    adduser -D -s /bin/sh -u 1000 -G appgroup appuser

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy application jar with proper ownership
COPY --from=build --chown=appuser:appgroup /app/target/agranelos-bff-*.jar app.jar

# Set environment variables with defaults
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV AZURE_FUNCTIONS_BASE_URL=""
ENV SPRING_PROFILES_ACTIVE="prod"

# Expose port
EXPOSE 8080

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
