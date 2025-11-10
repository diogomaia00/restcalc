
# Multi-stage build for optimized image size and security
# Define the first stage (builder) using JDK image with all development tools
FROM eclipse-temurin:24-jdk AS builder
# Set the working directory inside the container
WORKDIR /app
# Copy all project files from host to container (including source code, build files, etc.)
COPY . . 
# Execute Gradle build command: clean previous builds, build the service, skip tests for faster build
RUN ./gradlew service-calculator:clean service-calculator:build -x test

# Runtime stage - second stage using lightweight JRE image (no development tools)
FROM eclipse-temurin:24-jre
# Set the working directory for the runtime container
WORKDIR /app
# Copy only the built JAR file from the builder stage to keep runtime image minimal
COPY --from=builder /app/service-calculator/build/libs/*.jar app.jar

# Calculator service doesn't need to expose HTTP port (internal service communicating via Kafka)
# Define the command to run when container starts - execute the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]