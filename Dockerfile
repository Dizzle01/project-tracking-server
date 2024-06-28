# Image
FROM amazoncorretto:21

# JAR file path
ARG JAR_FILE=target/*.jar

# Copy JAR file into Docker image
COPY ${JAR_FILE} application.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/application.jar"]