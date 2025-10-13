# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (go-offline doesn't work perfectly, using resolve instead)
RUN ./mvnw dependency:resolve -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Environment variables (Railway injects actual values in production)
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DATABASE_URL=${DATABASE_URL}

# Run the Spring Boot application
CMD ["java", "-jar", "target/lms-backend-0.0.1-SNAPSHOT.jar"]