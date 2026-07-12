FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем собранный JAR (если вы собрали локально)
COPY app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]