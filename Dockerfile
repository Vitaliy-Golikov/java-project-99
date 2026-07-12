# Этап 1: Сборка проекта
FROM gradle:8.7-jdk21 AS build

WORKDIR /app

# Копируем все файлы проекта
COPY . .

# Даем права на выполнение gradlew
RUN chmod +x gradlew

# Собираем проект
RUN ./gradlew bootJar --no-daemon -x test

# Этап 2: Запуск приложения
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем JAR из этапа сборки
COPY --from=build /app/app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]