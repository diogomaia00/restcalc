
# Multi-stage build
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew service-calculator:clean service-calculator:build -x test

# Runtime stage
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /app/service-calculator/build/libs/*.jar app.jar

# Calculator service doesn't need to expose HTTP port
ENTRYPOINT ["java", "-jar", "app.jar"]