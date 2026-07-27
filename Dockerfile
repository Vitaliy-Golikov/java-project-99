FROM gradle:8.7-jdk21

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

CMD ["java", "-jar", "/app/build/libs/*.jar"]