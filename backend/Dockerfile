# Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run JAR
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/lms-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port and start app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
