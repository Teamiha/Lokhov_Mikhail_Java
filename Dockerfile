# Multi-stage build for smaller final image
FROM eclipse-temurin:11-jdk-alpine AS build

WORKDIR /app

# Install Maven (needed for build)
RUN apk add --no-cache maven

# Copy pom.xml first (for dependency caching)
COPY pom.xml ./

# Download dependencies (cached layer if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy JAR from build stage
COPY --from=build /app/target/vacation-calculator-*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

USER spring:spring

# Expose port
EXPOSE 8080

# Koyeb sets PORT env var, convert it to SERVER_PORT for Spring Boot
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]