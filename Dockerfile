# Use OpenJDK 21 JDK for building
FROM openjdk:21-jdk-slim AS build

WORKDIR /app
COPY . /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Install Maven dependencies and build the JAR file
RUN mvn -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Use a smaller OpenJDK image for the final stage
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/StezkaWeb-1.0-SNAPSHOT.jar /app/StezkaWeb.jar

# Expose the port that the app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/StezkaWeb.jar"]
