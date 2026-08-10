# meal-planner-rest

Internal REST API for nutrition goals and daily calorie-compliance
tracking. Backs the [`meal-planner-web`](../meal-planner-web/README.md)
application — not designed to be called by anything else, and has no
authentication layer of its own.

## Tech stack

- Java 17, Spring Boot 3.4.0
- Spring Data JPA, Spring Web, Spring Validation
- MySQL (runtime), H2 (tests)
- Lombok
- `spring-boot-starter-mail` is a declared dependency with SMTP config in
  `application.properties`, but nothing in the current codebase sends
  email — appears to be scaffolding for a feature not yet built.

## Running

```bash
./mvnw spring-boot:run
```

Default profile connects to MySQL at `localhost:3306`, database
`meal_planner_rest` (auto-created), user `root` / password `root` — edit
`src/main/resources/application.properties` to change. Fixed to run on
port **8081**.

A `prod` profile (`application-prod.properties`) points the datasource at
`host.docker.internal` instead of `localhost` — for running this service
in a container against MySQL on the host:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## API

### Nutrition Goals — `/api/v1/nutrition-goals`

| Method | Path | Description | Body |
|---|---|---|---|
| `POST` | `/api/v1/nutrition-goals` | Create a goal for a user (fails if one already exists) | `{ externalUserId, dailyCalorieTarget }` |
| `PUT` | `/api/v1/nutrition-goals` | Update the existing goal | same |
| `GET` | `/api/v1/nutrition-goals/{userId}` | Fetch a user's goal | — |

`dailyCalorieTarget` must be between 500 and 10000.

### Compliance Checks — `/api/v1/compliance-checks`

| Method | Path | Description | Body |
|---|---|---|---|
| `POST` | `/api/v1/compliance-checks` | Record a day's compliance result | `{ externalUserId, checkDate, totalCaloriesConsumed, targetedCalories }` |
| `GET` | `/api/v1/compliance-checks/{userId}/weekly` | Days within vs. over target, last 7 days | — |

`withinTarget` is computed server-side (`totalCaloriesConsumed <=
targetedCalories`), not accepted as input.

## Domain model

| Entity | Fields |
|---|---|
| `NutritionGoal` | `id`, `externalUserId`, `dailyCalorieTarget`, `createdOn`, `updatedOn` |
| `ComplianceCheck` | `id`, `externalUserId`, `checkDate`, `totalCaloriesConsumed`, `targetCalories`, `withinTarget`, `createdOn` |

`externalUserId` is a UUID owned by the calling system — this service has
no user model of its own and doesn't validate that the ID corresponds to
a real user.

## Testing

```bash
./mvnw test
```

Tests run against H2. JaCoCo is configured with a 70% line-coverage check,
but the suite currently only contains the default Spring context-load test
(`ApplicationTests`) — the coverage gate isn't yet meaningfully enforced.

