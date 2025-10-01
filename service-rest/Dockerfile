
# Multi-stage build
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew service-rest:clean service-rest:build -x test

# Runtime stage
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /app/service-rest/build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]