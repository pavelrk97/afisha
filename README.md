# Explore-Me

Веб-приложение для поиска и публикации городских событий — концерты, выставки,
встречи. Пользователи создают мероприятия, ищут интересное рядом, подают заявки
на участие и подписываются на любимых организаторов.

Внутри две связки сервисов: основное приложение `ewm-main-svc` со всей бизнес-логикой
и отдельный микросервис `stats-server`, который считает просмотры событий через
тонкий HTTP-клиент.

[![Build](https://github.com/pavelrk97/java-explore-with-me/actions/workflows/build.yml/badge.svg)](https://github.com/pavelrk97/java-explore-with-me/actions)

## Что сделано

- Двухсервисная архитектура (`ewm-main-svc` + `stats-server`) с разделёнными БД и общим DTO-модулем
- Spring Security + Keycloak: OAuth2 Resource Server, JWT с ролями `USER`/`ADMIN`
- Асинхронная отправка статистики через `@Async` с собственным `ThreadPoolTaskExecutor` —
  hot path не блокируется
- Liquibase для версионирования схемы БД вместо `schema.sql`
- springdoc-openapi: Swagger UI автогенерируется из контроллеров
- Тесты: JUnit 5 + Mockito (сервисы), `@WebMvcTest` + spring-security-test (контроллеры),
  Testcontainers (integration) — 37 тестов, 5 доменов покрыто
- CI на GitHub Actions: Ubuntu + Java 21 Corretto + `mvn clean verify`

## Стек

- Java 21, Spring Boot 3.4.1 (Web, Data JPA, Validation, Actuator, Security, OAuth2 Resource Server)
- PostgreSQL 16.1, H2 (для локальной разработки)
- Hibernate 6, Spring Data JPA
- Maven multi-module, Lombok
- Keycloak 25 как Identity Provider
- Docker Compose: 2 приложения + Keycloak + 2 БД с healthcheck'ами
- Liquibase, springdoc-openapi 2.6
- Тесты: JUnit 5, Mockito, Testcontainers, AssertJ
- Качество: Checkstyle, SpotBugs, JaCoCo
- CI: GitHub Actions

## Запуск

```bash
docker compose up --build
```

| Сервис | URL |
|---|---|
| ewm-main-svc | http://localhost:8080 |
| stats-server | http://localhost:9090 |
| Keycloak | http://localhost:8180 (admin/admin) |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Healthcheck | http://localhost:8080/actuator/health |

### Тестовые учётки в Keycloak

| Логин | Пароль | Роли |
|---|---|---|
| user1 | password | USER |
| admin1 | password | USER, ADMIN |

Realm: `ewm`, client: `ewm-client` (public, direct access grants).

## API

Документация автогенерируется через **springdoc-openapi**:
- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`

REST разделён на три уровня доступа по URL-префиксам:

| Уровень | Префикс | Доступ |
|---|---|---|
| Public | `/events`, `/categories`, `/compilations` | без токена |
| Private | `/users/{userId}/...` | JWT с ролью `USER` |
| Admin | `/admin/...` | JWT с ролью `ADMIN` |

## Качество кода

```bash
mvn -P check verify        # Checkstyle + SpotBugs
mvn -P coverage verify     # + JaCoCo report
```

CI прогоняет полный билд + тесты на каждый push (`.github/workflows/build.yml`).

## Roadmap

- [x] Liquibase, миграции схемы
- [x] springdoc-openapi, автогенерация Swagger
- [x] @Async для отправки stats
- [x] Spring Security + Keycloak
- [x] Тесты (unit + WebMvcTest + Testcontainers)
- [x] CI на GitHub Actions

