FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем все файлы проекта
COPY . .

# Даем права на выполнение gradlew
RUN chmod +x gradlew

# Собираем проект
RUN ./gradlew clean build -x test

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]