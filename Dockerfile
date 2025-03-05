# Use Maven with JDK 17 for building the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and dependency files first
COPY pom.xml .

# Download dependencies (this helps in caching layers)
RUN mvn dependency:go-offline

# Now copy the entire project
COPY . .

# Run Maven to build the application
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
