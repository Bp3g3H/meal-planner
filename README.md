# Meal Planner

A two-service Spring Boot application for meal logging, a shared group food
diary, a dish catalog, and nutrition-goal / calorie-compliance tracking.

## Architecture

- **`meal-planner-web`** — the user-facing application. Registration/login,
  groups, dish catalog, meal logging, group diary, and the nutrition
  goals/insights pages. Has its own MySQL database and its own users.
- **`meal-planner-rest`** — an internal REST API that owns nutrition goals
  and daily compliance checks, backed by a separate MySQL database. It has
  no concept of users of its own — `meal-planner-web` passes it a
  user-owned UUID (`externalUserId`).
- `meal-planner-web` calls `meal-planner-rest` through a Feign client
  (`NutritionServiceClient`), configured via `nutrition.service.url`
  (default `http://localhost:8081/api/v1`).
- A nightly scheduled job in `meal-planner-web` (`ComplianceCheckJob`, runs
  at 00:05) computes each user's total calories from the previous day's
  meal logs and posts a compliance check to `meal-planner-rest`.

## Repository layout

| Path | Description |
|---|---|
| [`meal-planner-web/`](meal-planner-web/README.md) | User-facing Spring Boot MVC app. |
| [`meal-planner-rest/`](meal-planner-rest/README.md) | Internal REST API for nutrition goals & compliance. |

Each module has its own README with setup, API details, and domain model —
this file covers only what spans both.

## Prerequisites

- JDK 17
- Maven 3.6+ (or use the included `mvnw` wrapper in each module)
- MySQL 8 reachable locally — both databases (`meal_planner`,
  `meal_planner_rest`) are auto-created on first run
  (`createDatabaseIfNotExist=true`), so no manual `CREATE DATABASE` needed

## Running locally

Start the REST service first — `meal-planner-web`'s nutrition pages and
nightly job depend on it, though the rest of the app (login, dishes, meal
logging, diary) works independently and degrades gracefully with a
"service unavailable" message if the REST service isn't reachable.

```bash
# 1. REST API — http://localhost:8081
cd meal-planner-rest
./mvnw spring-boot:run
```

```bash
# 2. Web app — http://localhost:8080
cd meal-planner-web
./mvnw spring-boot:run
```

Log in with the seeded admin account: `admin@example.com` / `admin123`
(created automatically on first run — see the web module's README for
details, and change this before any real deployment).

## Tech stack

| | meal-planner-web | meal-planner-rest |
|---|---|---|
| Framework | Spring Boot 3.4.0 | Spring Boot 3.4.0 |
| Java | 17 | 17 |
| View | Thymeleaf | — (API only) |
| Persistence | Spring Data JPA + MySQL | Spring Data JPA + MySQL |
| Auth | Spring Security (form login) | none |
| Inter-service | Feign client (caller) | — (callee) |
| Test DB | H2 | H2 |
| Coverage gate | JaCoCo, 70% line minimum | JaCoCo, 70% line minimum |
