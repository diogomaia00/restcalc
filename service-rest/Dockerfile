
# Multi-stage BUILD for optimized image size and security
# Define the first stage (builder) using JDK image with all development tools
FROM eclipse-temurin:24-jdk AS builder
# Set the working directory inside the container
WORKDIR /app
# Copy all project files from host to container (including source code, build files, etc.)
COPY . .
# Execute Gradle build command: clean previous builds, build the REST service, skip tests for faster build
RUN ./gradlew service-rest:clean service-rest:build -x test

# RUNTIME stage - second stage using lightweight JRE image (no development tools)
FROM eclipse-temurin:24-jre
# Set the working directory for the runtime container
WORKDIR /app
# Copy only the built JAR file from the builder stage to keep runtime image minimal
COPY --from=builder /app/service-rest/build/libs/*.jar app.jar

# Set environment variable for the port (can be overridden at runtime)
ENV PORT=8080
# Expose port 8080 to allow external HTTP connections to the REST API
EXPOSE 8080

# Define the command to run when container starts - execute the Java REST application
ENTRYPOINT ["java", "-jar", "app.jar"]