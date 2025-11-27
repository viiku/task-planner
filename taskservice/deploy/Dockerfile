FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

COPY ui/dist ./static
COPY target/*.jar app.jar

ENV SPRING_WEB_RESOURCES_STATIC_LOCATIONS="classpath:/static,file:/app/static/"

# Expose port (change if your app uses a different port)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]