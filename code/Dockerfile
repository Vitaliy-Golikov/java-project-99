FROM gradle:8.7-jdk21

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только папку code (где Gradle проект)
COPY code /app

# Даем права на выполнение gradlew
RUN chmod +x /app/gradlew

# Собираем проект
RUN ./gradlew installDist

# Запускаем приложение
CMD ./build/install/java-project-99/bin/java-project-99