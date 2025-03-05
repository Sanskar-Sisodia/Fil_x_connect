# Use Maven with JDK 17 for building the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the project files correctly (since your files are inside filxconnect/)
COPY filxconnect/ .

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JDK image for running the application
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
