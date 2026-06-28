# Afisha

Сервис для публикации городских событий: концерты, выставки, встречи. Пользователь
заводит мероприятие, ищет что-то интересное рядом, оставляет заявку на участие и
подписывается на организаторов.

Приложение поделено на два сервиса. Основной, `main-svc`, держит всю бизнес-логику.
Просмотры событий считает отдельный `stats-server`, к которому `main-svc` ходит
через тонкий HTTP-клиент.

[![Build](https://github.com/pavelrk97/afisha/actions/workflows/build.yml/badge.svg)](https://github.com/pavelrk97/afisha/actions)

## Стек

- Java 21, Spring Boot 3 (Web, Data JPA, Validation, Actuator, Security, OAuth2 Resource Server)
- PostgreSQL 16, H2 для локального запуска
- Spring Data JPA, Hibernate 6
- Keycloak 25 как Identity Provider, JWT с ролями `USER` и `ADMIN`
- Liquibase для миграций схемы
- springdoc-openapi для Swagger UI
- Maven (multi-module), Lombok
- Docker Compose: два приложения, Keycloak и две базы с healthcheck
- Тесты: JUnit 5, Mockito, Spring MVC Test, Testcontainers, AssertJ
- Качество кода: Checkstyle, SpotBugs, JaCoCo
- CI: GitHub Actions

## Как устроено

На каждый публичный запрос `main-svc` шлёт хит в `stats-server`. Отправка идёт
асинхронно, через `@Async` со своим пулом потоков, чтобы не тормозить основной
запрос. Контракт DTO вынесен в общий модуль `stat-dto`, его используют и клиент,
и сервер.

Модули:

- `main-svc` (порт 8080). REST поделён на три уровня доступа по префиксам URL.
- `stat-svc` собирает статистику и состоит из трёх частей: `stat-dto` (общие DTO),
  `stat-client` (HTTP-клиент для `main-svc`) и `stats-server` (порт 9090).

## Запуск

```bash
docker compose up --build
```

| Сервис | URL |
|---|---|
| main-svc | http://localhost:8080 |
| stats-server | http://localhost:9090 |
| Keycloak | http://localhost:8180 (admin/admin) |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Healthcheck | http://localhost:8080/actuator/health |

### Тестовые учётки в Keycloak

| Логин | Пароль | Роли |
|---|---|---|
| user1 | password | USER |
| admin1 | password | USER, ADMIN |

Realm: `afisha`, client: `afisha-client` (public, direct access grants).

## API

Документация собирается через springdoc-openapi:

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`

Эндпоинты разнесены по трём уровням доступа:

| Уровень | Префикс | Доступ |
|---|---|---|
| Public | `/events`, `/categories`, `/compilations` | без токена |
| Private | `/users/{userId}/...` | JWT с ролью `USER` |
| Admin | `/admin/...` | JWT с ролью `ADMIN` |

## Качество кода

```bash
mvn -P check verify        # Checkstyle + SpotBugs
mvn -P coverage verify     # плюс отчёт JaCoCo
```

Полный билд с тестами гоняется на каждый push (`.github/workflows/build.yml`).
