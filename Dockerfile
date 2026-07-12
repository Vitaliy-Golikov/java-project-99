FROM gradle:9.5.1-jdk21 AS build

WORKDIR /app

# Копируем ВСЁ содержимое папки app в корень /app
COPY app/ ./

RUN chmod +x gradlew
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]