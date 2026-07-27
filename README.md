### Hexlet tests and linter status:
[![Actions Status](https://github.com/Vitaliy-Golikov/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/Vitaliy-Golikov/java-project-99/actions)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Vitaliy-Golikov_java-project-99&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Vitaliy-Golikov_java-project-99)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Vitaliy-Golikov_java-project-99&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Vitaliy-Golikov_java-project-99)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Vitaliy-Golikov_java-project-99&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Vitaliy-Golikov_java-project-99)

# Менеджер задач

## Описание
Проект «Менеджер задач» — это веб-приложение для управления задачами.
Пользователи могут создавать, обновлять, назначать и отслеживать задачи, а также управлять их статусами и метками.

Основные возможности:

- Создание, редактирование и удаление задач.
- Назначение исполнителей на задачи.
- Управление статусами задач.
- Назначение и удаление меток (labels) у задач.
- REST API для интеграции с другими сервисами.

## Технологии

**Backend**
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate

**База данных**
- PostgreSQL

**Документация API**
- SpringDoc OpenAPI (Swagger)

**Тестирование**
- JUnit 5
- MockMvc

**Сборка**
- Gradle