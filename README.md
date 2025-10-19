




#### Build and Run Instructions for Docker

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

