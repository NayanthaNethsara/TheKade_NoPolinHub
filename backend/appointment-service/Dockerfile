# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copy project files
COPY pom.xml .
COPY src ./src

# Make mvnw executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy built jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port (change per service)
EXPOSE 8081

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
