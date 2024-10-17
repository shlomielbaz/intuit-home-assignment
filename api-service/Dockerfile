# Use an OpenJDK 17 base image for Maven to build the application
FROM maven:3.8.5-openjdk-17 AS BUILDER

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .

COPY src ./src

# Build the application using Maven
RUN mvn clean package

# Use an OpenJDK 17 base image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=BUILDER /app/target/players-0.0.1.jar /app/players-0.0.1.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/players-0.0.1.jar"]