# use the `eclipse-temurin` image with JDK 21 on Alpine Linux
FROM eclipse-temurin:21-jdk-alpine

# set the working directory in the container
# tells docker to start at this point when executing commands
WORKDIR /app

# copy the package file from mvn into the image
COPY target/healthcare-api-0.0.1-SNAPSHOT.jar healthcare-api.jar

# expose port environment variable 8080 to the outside world
EXPOSE 8080

# run the jar file
# the ENTRYPOINT instruction specifies a command that will always be executed when the container starts
ENTRYPOINT ["java", "-jar", "healthcare-api.jar"]

