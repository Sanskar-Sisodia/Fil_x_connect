# Use an official OpenJDK runtime as a base image
FROM maven:3.8.7-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Build the application using system-wide Maven
RUN mvn clean package -DskipTests

# Create a new image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
