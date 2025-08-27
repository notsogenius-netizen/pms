# Patient Management System

A microservices-based patient management system with PostgreSQL and MongoDB databases.

## Prerequisites

- Docker
- Docker Compose

## Quick Start

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
```

### 2. Start the Services

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 6000
- MongoDB database on port 6001
- Patient Service on port 8000
- Billing Service on port 8001 (HTTP) and 9090 (gRPC)

### 3. Access the Application

- Patient Service API: http://localhost:8000
- Billing Service HTTP API: http://localhost:8001
- Billing Service gRPC: localhost:9090
- PostgreSQL: localhost:6000
- MongoDB: localhost:6001

## Services Overview

| Service | Port | Protocol | Description |
|---------|------|----------|-------------|
| Patient Service | 8000 | HTTP | Patient management REST API |
| Billing Service | 8001 | HTTP | Billing management REST API |
| Billing Service | 9090 | gRPC | Billing management gRPC API |
| PostgreSQL | 6000 | TCP | Primary database |
| MongoDB | 6001 | TCP | Document database |

## Security Best Practices

✅ **DO:**
- Use strong, unique passwords in your `.env` file
- Keep your `.env` file secure and never commit it to version control
- Use different passwords for different environments (dev, staging, production)
- Regularly rotate your database passwords

❌ **DON'T:**
- Commit the `.env` file to version control
- Use default passwords in production
- Share your `.env` file with others
- Use the same passwords across different projects

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `POSTGRES_USER` | PostgreSQL username | `postgres` |
| `POSTGRES_PASSWORD` | PostgreSQL password | `postgres` |
| `POSTGRES_DB` | PostgreSQL database name | `patient_service_db` |
| `MONGO_USER` | MongoDB username | `mongo` |
| `MONGO_PASSWORD` | MongoDB password | `mongo` |
| `SPRING_PROFILES_ACTIVE` | Spring profile | `docker` |

## Development

### Rebuild and Restart

```bash
docker-compose down
docker-compose up -d --build
```

### View Logs

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs patient-service
docker-compose logs billing-service
docker-compose logs postgres
docker-compose logs mongo
```

### Stop Services

```bash
docker-compose down
```

## Project Structure

```
Patient Management System/
├── docker-compose.yml          # Main orchestration file
├── env.example                 # Environment variables template
├── .env                        # Your actual environment file (not in git)
├── .gitignore                  # Git ignore rules
├── README.md                   # This file
├── patient-service/            # Patient microservice
│   ├── Dockerfile             # Service container definition
│   ├── pom.xml                # Maven dependencies
│   └── src/                   # Java source code
└── billing-service/           # Billing microservice (gRPC)
    ├── Dockerfile             # Service container definition
    ├── pom.xml                # Maven dependencies
    └── src/                   # Java source code
```
