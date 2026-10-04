# Student Course Management System

A **microservices-based Student Course Management System** built with **Java, Spring Boot, Spring Cloud, Spring Security, MySQL, Docker, and Docker Compose**.

The system separates business functionality into independently deployable services and uses an API Gateway, service discovery, JWT-based authentication, service-to-service authentication, fault tolerance, and observability components.

---

## Architecture

```text
                              Client
                                |
                                v
                       +------------------+
                       |    API Gateway   |
                       |      :8080       |
                       +--------+---------+
                                |
             +------------------+------------------+
             |                  |                  |
             v                  v                  v
      Student Service     Course Service     Employee Service
         :8084               :8083               :8085
             |                  |                  |
             +------------------+------------------+
                                |
                         Service Discovery
                                |
                         +------+------+
                         |   Consul    |
                         |    :8500    |
                         +-------------+

                         Authentication
                                |
                                v
                       +----------------+
                       |  Auth Service  |
                       |     :8090      |
                       +----------------+

                       Other Services
                                |
        +-----------------------+-----------------------+
        |                       |                       |
        v                       v                       v
 Batch Service          Course Assignment      Student-Batch
    :8087                    :8086               Assignment :8088
        |                       |                       |
        +-----------------------+-----------------------+
                                |
                         Note Service :8089


                         Observability
                                |
                        OpenTelemetry / OTLP
                                |
              +-----------------+-----------------+
              |                 |                 |
              v                 v                 v
            Tempo             Alloy           Prometheus
              |                 |
              +--------+--------+
                       |
                       v
                    Grafana
```

---

## Services

| Service | Port | Responsibility |
|---|---:|---|
| **API Gateway** | `8080` | Single entry point for external requests and API routing |
| **Auth Service** | `8090` | Authentication, JWT generation, users, and roles |
| **Course Service** | `8083` | Course and technology management |
| **Student Service** | `8084` | Student management |
| **Employee Service** | `8085` | Employee management |
| **Course Assignment Service** | `8086` | Course assignment management |
| **Batch Service** | `8087` | Batch management |
| **Student-Batch Assignment Service** | `8088` | Student enrollment and batch assignment |
| **Note Service** | `8089` | Notes and technology-related note management |

### Infrastructure

| Component |                                   Port | Purpose |
|---|---------------------------------------:|---|
| **MySQL** |                                 `3306` | Persistent data storage |
| **Consul** |                                 `8500` | Service registration and discovery |
| **Grafana** |                                 `3000` | Observability dashboards |
| **Tempo** |                                 `3200` | Distributed tracing |
| **Alloy** |                                `12345` | Telemetry/log collection |
| **Prometheus** |                               `9090` | Metrics collection |


---

# Key Features

- Microservices architecture
- API Gateway
- Service discovery using Consul
- JWT-based authentication
- Role-based authorization
- Service-to-service authentication
- REST-based inter-service communication
- Centralized exception handling
- Correlation ID support
- Retry mechanism using Resilience4j
- Circuit breaker using Resilience4j
- Docker containerization
- Docker Compose orchestration
- MySQL persistence
- OpenTelemetry integration
- Distributed tracing
- Centralized logging and telemetry collection
- Grafana-based observability

---

# Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- Maven

## Spring Cloud

- Spring Cloud Gateway
- Spring Cloud Consul
- Consul Service Discovery

## Security

- Spring Security
- JWT
- Role-Based Access Control
- Internal Service Authentication

## Database

- MySQL 8.4

## Resilience

- Resilience4j
- Retry
- Circuit Breaker
- Fallback handling

## Observability

- OpenTelemetry
- OTLP
- Grafana Alloy
- Grafana Tempo
- Grafana
- Prometheus

## Containerization

- Docker
- Docker Compose

---

# Project Structure

```text
student_course_management/
│
├── .github/
│
├── api-gateway/
├── attendance-service/
├── auth-service/
├── batch-service/
├── course-assignment-service/
├── course-service/
├── employee-service/
├── note-service/
├── student-batch-assignment-service/
├── student-service/
│
├── monitoring/
├── database-backup/
├── logs/
│
├── .env
├── .env.example
├── .gitignore
├── docker-compose.yml
├── README.md
└── student_batch_assignment_db.sql
```



---

# Authentication and Authorization

The system uses a dedicated **Auth Service** for authentication.

Users authenticate through the Auth Service and receive a JWT.

```text
Client
  |
  | Login credentials
  v
Auth Service
  |
  | JWT
  v
Client
  |
  | Authorization: Bearer <JWT>
  v
API Gateway
  |
  | Authenticated request
  v
Target Microservice
```

The application uses role-based authorization.

Supported application roles include:

```text
ADMIN
TRAINER
STUDENT
EMPLOYEE
```

The system also protects internal service endpoints using an internal service authentication mechanism.

---

# API Gateway

The API Gateway is the main entry point for external clients.

```text
http://localhost:8080
```

Clients should communicate with the microservices through the Gateway rather than directly accessing individual services.

Example flow:

```text
Client
  |
  v
API Gateway :8080
  |
  +----> Course Service :8083
  |
  +----> Student Service :8084
  |
  +----> Employee Service :8085
  |
  +----> Batch Service :8087
  |
  +----> Other Services
```

The Gateway also participates in JWT authentication and request routing.

---

# Service Discovery with Consul

The project uses **HashiCorp Consul** for service registration and discovery.

Consul is available at:

```text
http://localhost:8500
```

Services register with Consul and discover other services through the service registry.

```text
                  Consul :8500
                       |
       +---------------+---------------+
       |               |               |
       v               v               v
 Student Service  Course Service  Batch Service
       |               |               |
       +---------------+---------------+
                       |
              Service Discovery
```

---

# Inter-Service Communication

Services communicate with each other using REST APIs.

The application uses Spring's `RestClient` for service-to-service communication where required.

For example:

```text
Course Assignment Service
          |
          | REST
          v
    Batch Service
          |
          | REST
          v
    Other Services
```

Service client classes keep inter-service communication separate from the core business logic.

---

# Resilience and Fault Tolerance

The application uses **Resilience4j** to handle failures in service-to-service communication.

## Retry

Temporary failures can be retried according to the configured retry policy.

```text
Service A
   |
   v
Service B
   |
 Failure
   |
   v
 Retry
   |
   +---- Success
   |
   +---- Failure
```

## Circuit Breaker

When a downstream service repeatedly fails, the circuit breaker can prevent continuous requests from reaching the unavailable service.

```text
Service A
   |
   v
Service B
   |
   X
Failure
   |
   v
Circuit Breaker
   |
   v
Fallback
```

This helps reduce cascading failures across the system.

---

# Observability

The project includes an observability stack based on OpenTelemetry and related tools.

The main components are:

- OpenTelemetry
- OTLP
- Grafana Alloy
- Grafana Tempo
- Prometheus
- Grafana

A simplified tracing flow is:

```text
Spring Boot Service
        |
        v
OpenTelemetry
        |
       OTLP
        |
        v
      Tempo
        |
        v
     Grafana
```

Logging and telemetry collection can be handled through Grafana Alloy.

Correlation IDs are used to help associate requests across multiple services.

---

# Docker Architecture

The application services are containerized using Docker.

The root `docker-compose.yml` defines the application containers, MySQL, Consul, networks, and persistent volumes.

```text
Docker Compose
      |
      +---- MySQL
      |
      +---- Consul
      |
      +---- API Gateway
      |
      +---- Auth Service
      |
      +---- Student Service
      |
      +---- Course Service
      |
      +---- Employee Service
      |
      +---- Batch Service
      |
      +---- Course Assignment Service
      |
      +---- Student-Batch Assignment Service
      |
      +---- Note Service
```

### Docker Networks

Two external Docker networks are used:

```text
microservices
monitoring
```

Create them before starting the application if they do not already exist:

```bash
docker network create microservices
docker network create monitoring
```

Then start the application:

```bash
docker compose up -d
```

---

# Prerequisites

Install the following:

- Java 21
- Docker
- Docker Compose
- Git

Maven does not need to be installed separately if the project uses the Maven Wrapper (`mvnw`).

---

# Configuration

The application uses environment variables for credentials, secrets, database URLs, and infrastructure configuration.

Create a local `.env` file from the example:

```bash
cp .env.example .env
```

Then update the values for your environment.

The project expects configuration values such as:

```env
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=your_jwt_expiration

INTERNAL_SERVICE_KEY=your_internal_service_key

CONSUL_HOST=consul
CONSUL_PORT=8500

OTEL_EXPORTER_OTLP_ENDPOINT=your_otlp_endpoint
```

Database URLs are also configured through environment variables, for example:

```env
AUTH_DB_URL=...
STUDENT_DB_URL=...
COURSE_DB_URL=...
EMPLOYEE_DB_URL=...
BATCH_DB_URL=...
COURSE_ASSIGNMENT_DB_URL=...
STUDENT_BATCH_ASSIGNMENT_DB_URL=...
NOTE_DB_URL=...
```



---

# Running the Project

## 1. Clone the repository

```bash
git clone https://github.com/Alinps/student-course-management-microservices.git
```

## 2. Enter the project

```bash
cd student_course_management
```

## 3. Create Docker networks

Because the Compose file uses external networks:

```bash
docker network create microservices
docker network create monitoring
```

If a network already exists, Docker will report that it already exists.

## 4. Create the environment file

```bash
cp .env.example .env
```

Update `.env` with the required values.

## 5. Start the application

```bash
docker compose up -d
```

## 6. Check container status

```bash
docker compose ps
```

## 7. View logs

All services:

```bash
docker compose logs -f
```

A specific service:

```bash
docker compose logs -f auth-service
```

## 8. Stop the application

```bash
docker compose down
```

---

# Service Ports

The application services expose the following ports:

```text
API Gateway                         8080
Auth Service                       8090
Course Service                     8083
Student Service                    8084
Employee Service                   8085
Course Assignment Service          8086
Batch Service                      8087
Student-Batch Assignment Service   8088
Note Service                       8089
MySQL                              3306
Consul                             8500
```

---

# Database

MySQL 8.4 is used as the database server.

The Docker Compose configuration persists MySQL data using:

```text
mysql_data
```

mounted at:

```text
/var/lib/mysql
```

The services use separate database URLs, allowing service-specific database configuration.

Example:

```text
MySQL
 |
 +---- Auth Database
 |
 +---- Student Database
 |
 +---- Course Database
 |
 +---- Employee Database
 |
 +---- Batch Database
 |
 +---- Course Assignment Database
 |
 +---- Student-Batch Assignment Database
 |
 +---- Note Database
```


---

# Local Development

Individual services can also be run outside Docker during development.

For example:

```bash
cd student-service
./mvnw spring-boot:run
```

Build a service:

```bash
./mvnw clean package
```

---


# License

This project is currently intended for educational, portfolio, and development purposes.