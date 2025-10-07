# Stage 1: Build with Maven + JDK 21
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/foodOrderApp-0.0.1-SNAPSHOT.jar app.jar

# Expose port
ENV PORT=8080
EXPOSE 8080

# Start Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
