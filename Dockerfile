# Этап 1: сборка (build)
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar --no-daemon

# Этап 2: запуск (runtime)
FROM eclipse-temurin:21-jre

WORKDIR /app

# Создаём непривилегированного пользователя для безопасности
RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --gid 1001 appuser

# Копируем собранный JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Переключаемся на непривилегированного пользователя
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]