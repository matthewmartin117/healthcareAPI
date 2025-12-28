# Healthcare API

A secure, HIPAA-compliant healthcare API for managing patients, clinical notes, and biological samples, integrated with a PHI redaction service using FastAPI. The project uses Spring Boot, PostgreSQL, JWT-based authentication, and Docker Compose for multi-container deployment.

---
## Full System Demo (AI + Backend + DevOps)

This project includes a complete end-to-end demo covering:

- JWT authentication and role-based authorization
- Secure CRUD workflows for healthcare data
- A forward-deployed AI microservice for PHI classification and redaction
- Containerized ML inference integrated into a production backend

**[Watch the full demo video](https://www.loom.com/share/63391c51f6cd4593b07c4919fe72a2b1)**

The demo highlights how machine learning is deployed as infrastructure rather than as a standalone experiment.

---

## Table of Contents
1. [Features](#features)
2. [Architecture](#architecture)
3. [Requirements](#requirements)
4. [Setup & Run](#setup--run)
    - [Docker Compose (Production-like)](#docker-compose-recommended)
    - [Local H2 (Development/Tests)](#local-h2-development--tests)
5. [API Endpoints](#api-endpoints)
6. [Testing](#testing)
7. [PHI Redaction Service](#phi-redaction-service)
8. [Postman Collection](#postman-collection)
9. [Environment Variables](#environment-variables)
10. [License](#license)

---

## Features
- **Security:** JWT-based authentication & role-based authorization (USER/ADMIN).
- **CRUD Operations:**
    - Patients
    - Clinical Notes
    - Biological Samples
- **Privacy:** PHI (Protected Health Information) redaction for clinical notes using a FastAPI microservice.
- **Deployment:** Multi-container Docker Compose setup (Spring Boot + Postgres + FastAPI).
- **Dev Tools:** Optional H2 database for local testing and Health check endpoints.

---

## Architecture

```text
[Postman / Frontend] --> [Spring Boot API] --> [Postgres DB]
                               |
                               v
                     [FastAPI PHI Redactor]
```

- **Spring Boot:** Handles all API logic, security, and persistence.
- **PostgreSQL:** Stores patients, clinical notes, and samples.
- **FastAPI:** Microservice performs PHI redaction before notes are saved.
- **Docker Compose:** Orchestrates multi-container deployment.

---

## Requirements
- Docker & Docker Compose
- Java 17+
- Maven 3.8+
- Python 3.10+ (for FastAPI service)
- *Optional:* Postman for API testing

---

## Setup & Run

### Docker Compose (Recommended)
1. **Build and start containers:**
   ```bash
   docker-compose up --build
   ```

2. **Services included:**
   - **App:** Spring Boot API at `http://localhost:8080`
   - **DB:** PostgreSQL database at `localhost:5432`
   - **PHI Redactor:** FastAPI service at `http://localhost:8000`

3. **Stop containers:**
   ```bash
   docker-compose down
   ```

### Local H2 (Development / Tests)
1. Update Spring profile to H2 in `application.properties`:
   ```properties
   spring.profiles.active=h2
   ```

2. Run Spring Boot app locally via Maven:
   ```bash
   mvn clean spring-boot:run
   ```

3. **Access:**
   - API available at `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`

---

## API Endpoints

**Note:** All endpoints require a valid JWT token. You must login first.

### Authentication
| Method | Endpoint | Role | Description |
| :--- | :--- | :--- | :--- |
| **POST** | `/auth/login` | Public | Returns JWT token |

### Patients
| Method | Endpoint | Role | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/patients` | USER/ADMIN | List all patients |
| **GET** | `/patients/{id}` | USER/ADMIN | Get patient by ID |
| **POST** | `/patients` | USER/ADMIN | Create new patient |
| **PUT** | `/patients/{id}` | ADMIN | Update patient |
| **DELETE** | `/patients/{id}` | ADMIN | Delete patient |

### Clinical Notes
| Method | Endpoint | Role | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/patients/{id}/clinical-notes` | USER/ADMIN | List notes for a patient |
| **GET** | `/patients/{id}/clinical-notes/{noteId}` | USER/ADMIN | Get note by ID |
| **POST** | `/patients/{id}/clinical-notes` | USER/ADMIN | Create note (Trigger PHI redaction) |
| **PUT** | `/patients/{id}/clinical-notes/{noteId}` | ADMIN | Update note |
| **DELETE** | `/patients/{id}/clinical-notes/{noteId}` | ADMIN | Delete note |

### Biological Samples
| Method | Endpoint | Role | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/patients/{id}/biological-samples` | USER/ADMIN | List samples |
| **GET** | `/patients/{id}/biological-samples/{id}` | USER/ADMIN | Get sample by ID |
| **POST** | `/patients/{id}/biological-samples` | USER/ADMIN | Create sample |
| **PUT** | `/patients/{id}/biological-samples/{id}` | ADMIN | Update sample |
| **DELETE** | `/patients/{id}/biological-samples/{id}` | ADMIN | Delete sample |

### Health Check
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/` | Returns "Application is running." |

---

## Testing

Use the H2 profile for unit/integration tests to avoid polluting the production database.

Run all tests via Maven:
```bash
mvn test
```

---

## PHI Redaction Service

This is a standalone FastAPI microservice running at `http://phi-redactor:8000/redact`.

**Request Model:**
```json
{
  "text": "Patient John Doe has a fever",
  "aggregate_redaction": true
}
```

**Response Model:**
```json
{
  "redacted_text": "Patient [REDACTED] has a fever"
}
```
*The Spring Boot `PHIRedactionService` calls this endpoint automatically before saving clinical notes.*

---

## Postman Collection

1. **Login:** Send a POST to `/auth/login`. Copy the returned token.
2. **Set Variable:** Store the token in a collection variable named `{{jwt}}`.
3. **Authorization:** Set the collection Authorization type to **Bearer Token** and use `{{jwt}}`.
4. **Test:**
   - **Patients:** Create, Read, Update, Delete.
   - **Notes:** Create a note with a name like "John Doe" in the text and verify the response returns "[REDACTED]".

---

## Environment Variables

| Variable | Default | Description |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `postgres` | Switch between `postgres` (Docker) or `h2` (Local). |
| `HUGGINGFACE_TOKEN` | *Required* | Token required for the PHI redaction model logic. |
| `POSTGRES_USER` | `postgres` | Database username. |
| `POSTGRES_PASSWORD` | `password` | Database password. |

---

## License
MIT License

