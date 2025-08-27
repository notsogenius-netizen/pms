# Patient Management System

A microservices-based patient management system with PostgreSQL, MongoDB, Kafka, and gRPC communication.

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Patient       │    │   Billing       │    │   Kafka         │
│   Service       │◄──►│   Service       │    │   (KRaft)       │
│   (Port 8000)   │    │   (Port 8001)   │    │   (Port 9092)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │   MongoDB       │    │   Event         │
│   (Port 6000)   │    │   (Port 6001)   │    │   Streaming     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Features

### Core Services
- **Patient Service** - REST API for patient management
- **Billing Service** - gRPC service for billing operations
- **Kafka Integration** - Asynchronous event streaming
- **Multi-Database Support** - PostgreSQL and MongoDB

### Communication Patterns
- **Synchronous** - gRPC calls between services
- **Asynchronous** - Kafka event publishing
- **REST APIs** - External client communication

## 📋 Prerequisites

- Docker
- Docker Compose

## 🛠️ Quick Start

### 1. Environment Setup

Copy the example environment file and configure your secrets:

```bash
cp env.example .env
```

Edit the `.env` file with your secure passwords:

```bash
# Database Configuration
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_postgres_password_here
POSTGRES_DB=patient_service_db

# MongoDB Configuration
MONGO_USER=mongo
MONGO_PASSWORD=your_secure_mongo_password_here

# Application Configuration
SPRING_PROFILES_ACTIVE=docker

# Kafka Configuration
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

### 2. Start the Services

```bash
docker-compose up -d
```

This will start:
- **PostgreSQL** database on port 6000
- **MongoDB** database on port 6001
- **Kafka** (KRaft mode) on port 9092
- **Patient Service** on port 8000
- **Billing Service** on port 8001 (HTTP) and 9090 (gRPC)

### 3. Verify Services

Check if all services are running:

```bash
docker-compose ps
```

## 🔌 API Endpoints

### Patient Service (REST API)

#### Base URL: `http://localhost:8000/api/v1/patients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/` | Get all patients |
| `GET` | `/{id}` | Get patient by ID |
| `POST` | `/` | Create new patient |
| `PUT` | `/{id}` | Update patient |
| `DELETE` | `/{id}` | Delete patient |
| `GET` | `/search?name={name}` | Search patients by name |
| `GET` | `/paginated?page={page}&size={size}` | Get patients with pagination |
| `GET` | `/statistics` | Get patient statistics |

#### Advanced Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/patients/advanced/bulk` | Create multiple patients |
| `GET` | `/api/v1/patients/advanced/sorted` | Get sorted patients |

### Billing Service (gRPC)

#### gRPC Port: `9090`

**Service:** `BillingService`

| Method | Request | Response | Description |
|--------|---------|----------|-------------|
| `CreateBillingAccount` | `BillingRequest` | `BillingResponse` | Create billing account for patient |

#### Billing Service (HTTP API)

#### Base URL: `http://localhost:8001`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/health` | Health check |

## 📊 Database Schema

### PostgreSQL (Patient Service)
```sql
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    address VARCHAR NOT NULL,
    date_of_birth DATE NOT NULL,
    registered_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### MongoDB (Billing Service)
```javascript
// Billing accounts collection
{
  "_id": ObjectId,
  "patientId": String,
  "accountId": String,
  "status": String,
  "createdAt": Date,
  "updatedAt": Date
}
```

## 🔄 Event Flow

### Patient Creation Flow
1. **REST API Call** → Patient Service
2. **Database Save** → PostgreSQL
3. **gRPC Call** → Billing Service (Create billing account)
4. **Kafka Event** → Publish patient creation event
5. **Response** → Return patient ID

### Event Topics
- `patient` - Patient lifecycle events
  - `PATIENT_CREATED`
  - `PATIENT_UPDATED`
  - `PATIENT_DELETED`

## 🔧 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_USER` | `postgres` | PostgreSQL username |
| `POSTGRES_PASSWORD` | `postgres` | PostgreSQL password |
| `POSTGRES_DB` | `patient_service_db` | PostgreSQL database name |
| `MONGO_USER` | `mongo` | MongoDB username |
| `MONGO_PASSWORD` | `mongo` | MongoDB password |
| `SPRING_PROFILES_ACTIVE` | `docker` | Spring profile |

### Service Configuration

#### Patient Service
```yaml
spring:
  kafka:
    bootstrap-servers: kafka:9092

billing:
  service:
    address: billing-service
    grpc:
      port: 9090
```

#### Billing Service
```yaml
grpc:
  server:
    port: 9090
```

## 🐳 Docker Services

| Service | Port | Description |
|---------|------|-------------|
| `postgres` | 6000 | PostgreSQL database |
| `mongo` | 6001 | MongoDB database |
| `kafka` | 9092 | Apache Kafka (KRaft mode) |
| `patient-service` | 8000 | Patient management service |
| `billing-service` | 8001, 9090 | Billing service (HTTP + gRPC) |

## 🧪 Testing

### Test Patient Creation
```bash
curl -X POST http://localhost:8000/api/v1/patients \
  -H "Content-Type: application/json" \
  -d '{
    "patient_name": "John Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St",
    "date_of_birth": "1990-01-01",
    "registered_date": "2025-08-27"
  }'
```

### Test gRPC Connection
```bash
# Check if billing service is accessible
curl http://localhost:8001/health
```

### Monitor Kafka Events
```bash
# Check Kafka logs
docker-compose logs kafka
```

## 📝 Logs

### View Service Logs
```bash
# Patient service logs
docker-compose logs patient-service

# Billing service logs
docker-compose logs billing-service

# Kafka logs
docker-compose logs kafka

# Database logs
docker-compose logs postgres
docker-compose logs mongo
```

## 🔍 Troubleshooting

### Common Issues

1. **gRPC Connection Failed**
   - Ensure billing service is running
   - Check if port 9090 is accessible
   - Verify service name resolution in Docker network

2. **Kafka Connection Failed**
   - Ensure Kafka is running
   - Check if port 9092 is accessible
   - Verify KRaft mode configuration

3. **Database Connection Failed**
   - Ensure databases are running
   - Check environment variables
   - Verify network connectivity

### Health Checks
```bash
# Check all services
docker-compose ps

# Check service health
curl http://localhost:8000/api/v1/health
curl http://localhost:8001/health
```

## 🚀 Development

### Adding New Services
1. Create service directory
2. Add Dockerfile
3. Update docker-compose.yml
4. Configure networking
5. Add environment variables

### Adding New gRPC Methods
1. Update proto files
2. Regenerate gRPC stubs
3. Implement service methods
4. Update client code

### Adding New Kafka Topics
1. Define event schema
2. Update producer code
3. Add consumer if needed
4. Test event flow

## 📚 Technologies Used

- **Java 21** - Programming language
- **Spring Boot 3.5.5** - Application framework
- **Spring Data JPA** - Database access
- **gRPC** - Inter-service communication
- **Apache Kafka** - Event streaming
- **PostgreSQL** - Primary database
- **MongoDB** - Document database
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
