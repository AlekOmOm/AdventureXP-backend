# Use a specific version of OpenJDK
FROM openjdk:17-jdk-slim

# Create a directory for the application
RUN mkdir /app

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download the dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Package the application
RUN ./mvnw clean package

# Copy the JAR file to the app directory
COPY target/AdventureXP-Backend-0.0.1-SNAPSHOT.jar /app/AdventureXP-Backend-0.0.1-SNAPSHOT.jar

# Set the entry point
CMD ["java", "-jar", "/app/AdventureXP-Backend-0.0.1-SNAPSHOT.jar"]