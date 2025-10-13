# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml from backend
COPY backend/.mvn/ .mvn/
COPY backend/mvnw backend/pom.xml ./

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:resolve -B

# Copy source code from backend
COPY backend/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Environment variables (Railway will inject these)
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DATABASE_URL=${DATABASE_URL}

# Run the Spring Boot application
CMD ["java", "-jar", "target/lms-backend-0.0.1-SNAPSHOT.jar"]