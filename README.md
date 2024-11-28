# Task Management System

## Описание

Это система управления задачами, предназначенная для работы с проектами, пользователями, задачами, комментариями и историей изменений.
Проект разработан для обучения и практического применения технологий Java, Spring Boot и PostgreSQL, а также для изучения архитектурных подходов.

## Технологии

Язык программирования: Java
Фреймворки: Spring Boot, Spring DATA JPA, Spring Web, Hibernate
База данных: PostgreSQL
API: REST, Swagger
Тестирование: JUnit, Mockito
Прочее: ModelMapper, DTO, Maven, Git, Validation, JSONB

## Основной функционал

Управление пользователями, задачами и проектами.
Поддержка комментариев и истории изменений задач.
Реализация REST API для интеграции с внешними сервисами.
Валидация данных на стороне сервера с использованием Bean Validation.

## Установка

1. Клонируйте репозиторий:

    ```bash
    git clone https://github.com/orphography/myproject.git
    ```

2. Перейдите в папку проекта:

    ```bash
    cd myproject
    ```

3. Настройте ваше окружение:
    - Установите PostgreSQL и создайте базу данных;
    - Убедитесь, что у вас настроены зависимости для Maven;

4. Запустите приложение:

    ```bash
    mvn spring-boot:run
    ```

5. Откройте браузер и перейдите по адресу:

    ```
    http://localhost:8080
    ```

## Использование

- **GET /api/tasks** — Получить все задачи.
- **GET /api/tasks/{id}** — Получить задачу по ID.
- **POST /api/tasks** — Создать новую задачу.
- **PUT /api/tasks/{id}** — Обновить задачу.
- **DELETE /api/tasks/{id}** — Удалить задачу.

Пример запроса для создания задачи:

```json
{
  "title": "New Task",
  "description": "Description of the task",
  "priority": "HIGH",
  "status": "PENDING"
}
