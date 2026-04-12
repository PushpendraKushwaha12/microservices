FROM eclipse-temurin:17-jdk-alpine
LABEL authors="Pushpendra Kushwaha"
WORKDIR /app
COPY target/user-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]