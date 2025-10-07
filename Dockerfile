# Stage 1: Build the JAR using Maven
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Stage 2: Create lightweight runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/foodOrderApp-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render sets PORT environment variable)
ENV PORT=8080
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
