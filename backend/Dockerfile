# ========================================
# Stage 1: Build JAR using Maven
# ========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build JAR without tests
RUN mvn clean package -DskipTests

# ========================================
# Stage 2: Run the JAR
# ========================================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the JAR from the build stage (matches your Maven output)
COPY --from=build /app/target/lms-backend.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
