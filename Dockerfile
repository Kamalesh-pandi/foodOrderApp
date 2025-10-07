# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy your Spring Boot JAR into the container
COPY target/foodOrderApp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Use environment PORT variable if available (Render provides it)
ENV PORT=8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "foodOrderApp-0.0.1-SNAPSHOT.jar"]
