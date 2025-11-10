# RESTful Calculator API

A microservices-based RESTful API that provides basic calculator functionalities (addition, subtraction, multiplication, and division) with support for arbitrary precision signed decimal numbers.

## Architecture

This project consists of two Spring Boot microservices:
- **REST Service** (`service-rest`) - Handles HTTP requests and exposes the RESTful API
- **Calculator Service** (`service-calculator`) - Performs the actual calculations
- **Apache Kafka** - Facilitates asynchronous communication between services

## Technology Stack

- **Java 24** - Programming language
- **Gradle** - Build automation and dependency management
- **Spring Boot** - Application framework and embedded server
- **Apache Kafka** - Message broker for inter-service communication
- **Docker & Docker Compose** - Containerization and orchestration
- **OpenAPI** - API documentation and specification

## Prerequisites

Before building and running this application, ensure you have the following installed:

gi- **Java 24** 
- **Docker** and **Docker Compose**
- **Git** (for cloning the repository)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/diogomaia00/restcalc.git
cd restcalc
```

### 2. Build the Application

Build both services using Gradle:

```bash
./gradlew build
```

This command will:
- Compile the source code for both services
- Run unit tests
- Generate JAR files in the `build/libs/` directories

### 3. Run with Docker Compose (Recommended)

The easiest way to run the entire application stack is using Docker Compose:

```bash
docker-compose up --build
```

This command will:
- Build Docker images for both services
- Start Apache Kafka
- Start the REST service on port 8080
- Start the Calculator service
- Set up the complete microservices environment

### 4. Alternative: Run Services Individually

If you prefer to run services individually (useful for development):

#### Start Kafka

```bash
docker run -d \
  --name kafka-container \
  -p 9092:9092 \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES='broker,controller' \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS='1@localhost:9093' \
  -e KAFKA_LISTENERS='PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093' \
  -e KAFKA_INTER_BROKER_LISTENER_NAME='PLAINTEXT' \
  -e KAFKA_ADVERTISED_LISTENERS='PLAINTEXT://localhost:9092' \
  -e KAFKA_CONTROLLER_LISTENER_NAMES='CONTROLLER' \
  -e KAFKA_LOG_DIRS='/tmp/kraft-combined-logs' \
  -e KAFKA_AUTO_CREATE_TOPICS_ENABLE='true' \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  apache/kafka:latest
```

#### Start Calculator Service

```bash
cd service-calculator
../gradlew bootRun
```

#### Start REST Service

```bash
cd service-rest
../gradlew bootRun
```

## API Usage

Once the services are running:

- REST API will be available at `http://localhost:8080` (check the available endpoints below).

- UI interaction is available through Swagger at `http://localhost:8080/swagger-ui/index.html`

- Kafka monitoring is available at `http://localhost:8081`

### Available Endpoints

- **Addition**: `GET /add?op1={number1}&op2={number2}`
- **Subtraction**: `GET /sub?op1={number1}&op2={number2}`
- **Multiplication**: `GET /mul?op1={number1}&op2={number2}`
- **Division**: `GET /div?op1={number1}&op2={number2}`

### Example Requests

```bash
# Addition: 1 + 2
curl "http://localhost:8080/add?op1=1&op2=2"
# Response: {"result": 3}

# Subtraction: 10 - 3
curl "http://localhost:8080/sub?op1=10&op2=3"
# Response: {"result": 7}

# Multiplication: 4 * 5
curl "http://localhost:8080/mul?op1=4&op2=5"
# Response: {"result": 20}

# Division: 15 / 3
curl "http://localhost:8080/div?op1=15&op2=3"
# Response: {"result": 5}
```

### API Documentation

The API follows OpenAPI 3.1.0 specification. You can find the complete API specification in `service-rest/src/main/resources/api.yaml`.

## Testing

Run unit tests for all services:

```bash
./gradlew test
```

Run tests for a specific service:

```bash
# REST service tests
./gradlew :service-rest:test

# Calculator service tests
./gradlew :service-calculator:test
```

## Development

### Project Structure

```
restcalc/
├── build.gradle                # Root build configuration
├── compose.yaml                # Docker Compose configuration
├── service-rest/               # REST API service
│   ├── build.gradle
│   ├── Dockerfile
│   └── src/
└── service-calculator/         # Calculator service
    ├── build.gradle
    ├── Dockerfile
    └── src/
```

### Configuration

Services are configured through `application.properties` files:
- **REST Service**: Port 8080, Kafka producer configuration
- **Calculator Service**: Kafka consumer configuration, no web server

Environment variables can override default configurations:
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address (default: localhost:9092)

## Stopping the Application

### Docker Compose

```bash
docker-compose down
```

### Individual Services

Stop services with `Ctrl+C` and clean up Kafka container:

```bash
docker stop kafka-container
docker rm kafka-container
```

## Troubleshooting

### Common Issues

1. **Port 8080 already in use**: Stop any services using port 8080 or change the port in `service-rest/src/main/resources/application.properties`

2. **Kafka connection issues**: Ensure Kafka is running and accessible on the configured port

3. **Java version mismatch**: Update the Java version in `build.gradle` to match your installed version

4. **Docker build issues**: Ensure Docker daemon is running and you have sufficient disk space

### Logs

View application logs:

```bash
# Docker Compose logs
docker-compose logs -f

# Individual service logs
docker-compose logs -f rest-service
docker-compose logs -f calculator-service
```