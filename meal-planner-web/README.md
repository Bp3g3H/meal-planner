# meal-planner-web

Spring Boot + Thymeleaf application for meal logging, a shared group food
diary, a dish catalog, and nutrition-goal tracking. Nutrition
goals/insights are delegated to
[`meal-planner-rest`](../../testAndReadme/mnt/user-data/outputs/meal-planner-rest/README.md) over HTTP.

> This README replaces an earlier version that described a custom
> session/cookie auth flow and H2-by-default setup. The current code uses
> Spring Security form login and MySQL by default — this file reflects
> what's actually there now.

## Tech stack

- Java 17, Spring Boot 3.4.0
- Thymeleaf (+ `thymeleaf-extras-springsecurity6`)
- Spring Security (form login)
- Spring Data JPA
- OpenFeign (calls `meal-planner-rest`)
- MySQL (runtime), H2 (tests)
- Lombok

## Running

```bash
./mvnw spring-boot:run
```

Connects to MySQL at `localhost:3306`, database `meal_planner`
(auto-created), user `root` / password `root` — edit
`src/main/resources/application.properties` to change. Runs on the default
Spring Boot port, **8080**.

Needs `meal-planner-rest` reachable at `nutrition.service.url` (default
`http://localhost:8081/api/v1`) for the nutrition-goals and
nutrition-insights pages — everything else (login, dishes, meal logging,
diary) works independently of it.

## Authentication & authorization

Handled by Spring Security (`WebMvcConfiguration`):

- Form login at `/login`, using **email** as the username parameter
- Public without login: `/`, `/login`, `/register`, `/error`, `/dishes`
- `/admin/**` requires the `ADMIN` authority
- Everything else requires an authenticated session

**Seeded admin (dev only):** `admin@example.com` / `admin123` — created
automatically by `DataSeederConfig` the first time the app starts with an
empty user table. Change or remove before any real deployment.

## Features

- Registration with a group choice: create a new group, join an existing
  one (password required if private), or start with an auto-created
  personal ("dummy") group
- Upgrade a dummy group to a named public/private group later, at `/group`
- Dish catalog (`/dishes`); admin CRUD for dishes at `/admin/dishes`
- Meal logging (`/meals/log`) with dish, meal type, portion size, and
  notes; edit/delete your own entries; shared group diary at
  `/meals/diary`
- Nutrition goals (`/nutrition-goals`) — set a daily calorie target,
  stored via `meal-planner-rest`
- Nutrition insights (`/nutrition-insights`) — weekly compliance summary
  plus a "trending dish" (most-logged dish across the group in the past
  week, refreshed every 30 minutes)
- Admin user management (`/admin/users`) — list users, change roles
- Nightly scheduled job (00:05) totals each user's calories from the
  previous day's meal logs and records a compliance check via
  `meal-planner-rest`

## Domain model

| Entity | Key fields |
|---|---|
| `User` | `username`, `email`, `password` (BCrypt), `role`, `@ManyToOne Group` |
| `Group` | `name`, `password` (BCrypt join code, nullable for public groups), `isDummy`, `isPublic` |
| `Dish` | `name`, `description`, `calories`, `category`, `imageUrl`, `@ManyToOne createdBy User` |
| `MealLog` | `@ManyToOne User`, `@ManyToOne Dish`, `mealType`, `portionSize`, `loggedInOn`, `notes` |


## Testing

```bash
./mvnw test
```

Tests run against H2. Same JaCoCo 70% line-coverage gate as
`meal-planner-rest` — currently only the default context-load test exists
(`MealPlannerApplicationTests`), so the gate isn't yet meaningfully
enforced.
