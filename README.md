# Afisha

A service for finding parties, house concerts, jam sessions and other small local
gigs. Users create events, browse what is happening nearby, request to join and
follow the organizers they like.

The app is split into two services. The main one, `main-svc`, holds all the
business logic. A separate `stats-server` counts event views, and `main-svc` talks
to it through a thin HTTP client.

[![Build](https://github.com/pavelrk97/afisha/actions/workflows/build.yml/badge.svg)](https://github.com/pavelrk97/afisha/actions)

## Stack

- Java 21, Spring Boot 3 (Web, Data JPA, Validation, Actuator, Security, OAuth2 Resource Server)
- PostgreSQL 16, H2 for local runs
- Spring Data JPA, Hibernate 6
- Keycloak 25 as the identity provider, JWT with `USER` and `ADMIN` roles
- Liquibase for schema migrations
- springdoc-openapi for Swagger UI
- Maven (multi-module), Lombok
- Docker Compose: two apps, Keycloak and two databases with healthchecks
- Tests: JUnit 5, Mockito, Spring MVC Test, Testcontainers, AssertJ
- Code quality: Checkstyle, SpotBugs, JaCoCo
- CI: GitHub Actions

## How it works

On every public request `main-svc` sends a hit to `stats-server`. The call is
asynchronous, via `@Async` with its own thread pool, so it does not slow down the
main request. The DTO contract lives in a shared `stat-dto` module used by both the
client and the server.

Modules:

- `main-svc` (port 8080). The REST API is split into three access levels by URL prefix.
- `stat-svc` collects statistics and has three parts: `stat-dto` (shared DTOs),
  `stat-client` (HTTP client for `main-svc`) and `stats-server` (port 9090).

## Run

```bash
docker compose up --build
```

| Service | URL |
|---|---|
| main-svc | http://localhost:8080 |
| stats-server | http://localhost:9090 |
| Keycloak | http://localhost:8180 (admin/admin) |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Healthcheck | http://localhost:8080/actuator/health |

### Test accounts in Keycloak

| Login | Password | Roles |
|---|---|---|
| user1 | password | USER |
| admin1 | password | USER, ADMIN |

Realm: `afisha`, client: `afisha-client` (public, direct access grants).

## API

Docs are generated with springdoc-openapi:

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`

Endpoints are grouped into three access levels:

| Level | Prefix | Access |
|---|---|---|
| Public | `/events`, `/categories`, `/compilations` | no token |
| Private | `/users/{userId}/...` | JWT with `USER` role |
| Admin | `/admin/...` | JWT with `ADMIN` role |

## Code quality

```bash
mvn -P check verify        # Checkstyle + SpotBugs
mvn -P coverage verify     # plus JaCoCo report
```

A full build with tests runs on every push (`.github/workflows/build.yml`).
