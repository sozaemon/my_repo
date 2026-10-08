# --- Stage 1: Build the application ---
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Pack the application into a fat JAR (skipping tests for build speed)
RUN mvn clean package -DskipTests

# --- Stage 2: Deploy and run ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as a non-root user for security compliance
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy only the compiled JAR file from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose Spring Boot's default port
EXPOSE 8080

# Optimize JVM settings for container awareness
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
