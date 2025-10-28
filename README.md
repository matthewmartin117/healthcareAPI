




## Build and Run Instructions for Docker

to build the Docker image:
```bash
docker build -t healthcare-api:latest .
```
to run the Docker container:
```bash
docker run -p 8080:8080 healthcare-api
```

# multi stage build example:
## Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

## Stage 2: Create the runtime image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/healthcare-api-0.0.1-SNAPSHOT.jar healthcare-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "healthcare-api.jar"]

# test the multi stage build
# to build the Docker image:
```bash
docker build -t healthcare-api:latest .
```
# to run the Docker container:
```bash
docker run -p 8080:8080 healthcare-api
```


## AWS Deployment Guide (Elastic Beanstalk + Docker Compose)
### Overview

This project is fully containerized and deployable on AWS Elastic Beanstalk using multi-architecture Docker images and a multi-container docker-compose.yml configuration.
The deployment includes both the Spring Boot backend and a PostgreSQL database, ensuring portability and production readiness.

### 1️⃣ Build and Push Multi-Architecture Image

Elastic Beanstalk supports x86_64 and ARM64 platforms.
To ensure compatibility, build the Docker image for both using Docker Buildx:

docker buildx build --platform linux/amd64,linux/arm64 -t <ECR_REPO_URL>:latest --push .


✅ This builds and pushes a multi-arch image directly to AWS ECR in one step.
Elastic Beanstalk automatically pulls the correct architecture based on its EC2 instance type.

###  Create AWS Resources
Elastic Container Registry (ECR)

Create a private ECR repository to host your image:

aws ecr create-repository --repository-name healthcare-api


Authenticate Docker to ECR:

aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <ECR_URL>


Your final image should appear in the ECR Console after pushing.

### Prepare Docker Compose for Elastic Beanstalk

Example docker-compose.yml:

version: "3.9"

services:
  db:
    image: postgres:17
    environment:
      POSTGRES_USER: healthcare_user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: healthcare_api
    volumes:
      - db_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U healthcare_user -d healthcare_api"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    image: <ECR_REPO_URL>:latest
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/healthcare_api
      SPRING_DATASOURCE_USERNAME: healthcare_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: update

volumes:
  db_data:


⚙️ This file defines a multi-container environment:

app = your Spring Boot service

db = a Postgres container

volumes = data persistence

healthcheck = ensures DB is ready before app starts

### Deploy to Elastic Beanstalk
Step 1 — Initialize Environment

From your project root:

eb init


Choose your AWS region

Select platform: Docker

Select your Elastic Beanstalk app or create a new one

Step 2 — Deploy

Make sure your docker-compose.yml is in the root directory, then run:

eb create healthcare-env --platform docker


If you already have an environment:

eb deploy


Elastic Beanstalk will:

Pull your image from ECR

Create an EC2 instance

Spin up containers defined in docker-compose.yml

Attach a load balancer and security group automatically

### IAM Roles and Permissions

Make sure your Elastic Beanstalk service role has:

AmazonEC2ContainerRegistryReadOnly

AmazonS3FullAccess (if you store config or static files)

CloudWatchLogsFullAccess (for monitoring logs)

And your EC2 instance profile includes:

AmazonEC2ContainerServiceforEC2Role

AmazonRDSFullAccess (if you’re using AWS-managed DBs)

### Verify Deployment

Once deployed:

eb status
eb open


Elastic Beanstalk will open your live URL (e.g. http://healthcare-env.us-east-1.elasticbeanstalk.com).


