# Gymnasios

Gymnasios is a workout tracking application focused on a simple and practical training flow: create a plan, define workout templates, add exercises, start a workout session, log completed sets, finish the session, and view workout history.

The project is built as a portfolio-oriented MVP. The main goal is not to provide advanced analytics or recommendation algorithms, but to show a clean end-to-end application with authentication, microservice backend, persistence, API gateway, and a mobile user interface.

---

## MVP Goal

The goal of the MVP is to provide a minimal workout tracking flow:

- user registration and login
- creating training plans
- creating workout templates inside training plans
- adding exercise templates to workout templates
- starting a workout session from a workout template
- filling completed sets during a workout session
- finishing a workout session
- viewing workout history

---

## MVP Scope

### Included in MVP

- basic authentication
- CRUD for training plans
- CRUD for workout templates
- CRUD for exercise templates
- workout session start/finish flow
- set logging during training
- simple user interface for basic operations

### Not included in MVP

- analytics
- progression recommendations
- statistics dashboards
- advanced workout planning logic
- social features

---

## Tech Stack

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
- PostgreSQL
- Redis
- Docker / Docker Compose
- Maven

### Mobile

- Kotlin
- Jetpack Compose
- Material 3
- Retrofit
- OkHttp
- Coroutines / Flow

---

## Architecture Overview

The backend is split into separate services. Requests from the mobile app go through the API Gateway, which routes traffic to the correct service.

```text
Mobile App
   |
   v
API Gateway
   |
   +-- user-service      -> PostgreSQL
   |
   +-- workout-service   -> PostgreSQL
   |
   +-- Redis             -> token/session-related infrastructure
   |
   +-- Eureka            -> service discovery
```

### Main backend modules

| Module | Responsibility |
| --- | --- |
| `api-gateway` | Single entry point for the client, routing, authentication-related filtering |
| `discovery-server` | Eureka service discovery |
| `user-service` | Registration, login, users, authentication data |
| `workout-service` | Training plans, workout templates, exercise templates, workout sessions, set logging |
| `config-server` | Optional centralized configuration, depending on the current environment setup |

---

## Main Domain Flow

```text
User
  creates Training Plan
    creates Workout Template
      adds Exercise Templates
        starts Workout Session
          logs completed sets
            finishes session
              sees workout history
```

### Templates vs Sessions

The project separates planned training structure from real workout execution:

- **Training plan** - groups workouts into one plan.
- **Workout template** - defines a planned workout inside a training plan.
- **Exercise template** - defines planned exercises, sets, reps, rest times and notes.
- **Workout session** - represents a real workout started from a template.
- **Set session** - represents completed sets filled during training.

This separation keeps the app flexible: templates describe what should be done, while sessions store what was actually completed.

---

## Repository Structure

Example structure:

```text
gymnasios/
  api-gateway/
  discovery-server/
  user-service/
  workout-service/
  config-server/
  docker-compose.yml
  .env.example

gymnasios-mobile/
  app/
  build.gradle.kts
  settings.gradle.kts
```

The exact structure may differ depending on whether backend and mobile are stored in one repository or separate repositories.

---

## Environment Variables

Secrets and environment-specific values should not be committed to the repository. Use a local `.env` file based on `.env.example`.

### Example `.env`

```env
# Spring
SPRING_PROFILES_ACTIVE=local

# Service discovery
EUREKA_DEFAULT_ZONE=http://discovery-server:8761/eureka/

# API Gateway
GATEWAY_PORT=8080

# User database
USER_DB_NAME=gymnasios_user
USER_DB_USERNAME=gymnasios
USER_DB_PASSWORD=gymnasios_password
USER_DB_URL=jdbc:postgresql://user-db:5432/gymnasios_user

# Workout database
WORKOUT_DB_NAME=gymnasios_workout
WORKOUT_DB_USERNAME=gymnasios
WORKOUT_DB_PASSWORD=gymnasios_password
WORKOUT_DB_URL=jdbc:postgresql://workout-db:5432/gymnasios_workout

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# JWT
JWT_SECRET=replace_with_a_strong_secret_key
JWT_ACCESS_TOKEN_EXPIRATION_MS=900000
JWT_REFRESH_TOKEN_EXPIRATION_MS=604800000

# Mail - optional, required only if email verification/password reset is enabled
MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USERNAME=example@example.com
MAIL_PASSWORD=replace_with_mail_password
MAIL_FROM=no-reply@gymnasios.app
```

For production or public deployment, replace all example values with real secrets stored in the hosting provider environment variables. Do not store production secrets in GitHub.

---

## How to Run Backend Locally

### 1. Clone the repository

```bash
git clone https://github.com/your-username/gymnasios.git
cd gymnasios
```

### 2. Create local environment file

```bash
cp .env.example .env
```

Then fill the required values in `.env`.

### 3. Start infrastructure and services

```bash
docker compose up --build
```

Depending on the current setup, the command may start:

- PostgreSQL for `user-service`
- PostgreSQL for `workout-service`
- Redis
- Eureka discovery server
- API Gateway
- backend microservices

### 4. Verify services

Common local URLs:

| Service | URL |
| --- | --- |
| API Gateway | `http://localhost:8080` |
| Eureka Dashboard | `http://localhost:8761` |
| User Service | `http://localhost:8081` |
| Workout Service | `http://localhost:8082` |

Ports may differ depending on local configuration.

---

## How to Run Backend Without Docker

Start required infrastructure first:

- PostgreSQL for `user-service`
- PostgreSQL for `workout-service`
- Redis

Then run services in this order:

```bash
cd discovery-server
mvn spring-boot:run
```

```bash
cd ../api-gateway
mvn spring-boot:run
```

```bash
cd ../user-service
mvn spring-boot:run
```

```bash
cd ../workout-service
mvn spring-boot:run
```

If the project uses `config-server`, start it before the other services or configure services to run with local configuration files.

---

## How to Run Mobile App

### 1. Open the Android project

Open the mobile project in Android Studio.

### 2. Configure backend URL

For Android Emulator, the host machine is available as:

```text
http://10.0.2.2:8080/
```

Example local configuration:

```properties
API_BASE_URL=http://10.0.2.2:8080/
```

Depending on the current implementation, this value may be placed in `local.properties`, `gradle.properties`, or another local configuration file. The backend URL should not be hardcoded for production builds.

### 3. Run the app

Start the backend first, then run the Android app from Android Studio.

---

## API Overview

The exact endpoint names may differ slightly depending on the current implementation, but the main resource groups are:

```text
/api/v1/auth
/api/v1/users
/api/v1/training-plans
/api/v1/workout-templates
/api/v1/exercise-templates
/api/v1/workout-sessions
/api/v1/exercise-sessions
/api/v1/set-sessions
```

Example flow:

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/training-plans
POST /api/v1/workout-templates
POST /api/v1/exercise-templates
POST /api/v1/workout-sessions
POST /api/v1/set-sessions
PATCH /api/v1/workout-sessions/{id}/finish
GET /api/v1/workout-sessions/history
```

---

## Testing

Run backend tests with Maven:

```bash
mvn test
```

Or run tests for a selected module:

```bash
cd workout-service
mvn test
```

The project should include tests for key business logic, validation, ownership checks, and API flows.

---

## Security Notes

- JWT tokens are used for authentication.
- Protected resources should be available only to the owner of the data.
- Secrets should be provided through environment variables.
- Production credentials must never be committed to the repository.
- Public demo deployments should use separate demo credentials and isolated databases.

---

## Current Status

The project is focused on completing a clean MVP suitable for portfolio presentation. The priority is to keep the application simple, understandable, and reliable instead of adding advanced features too early.

### MVP priority

1. Authentication
2. Training plan CRUD
3. Workout template CRUD
4. Exercise template CRUD
5. Starting workout sessions
6. Logging completed sets
7. Finishing workout sessions
8. Viewing workout history
9. Simple mobile UI for the full flow

---

## Future Improvements

Possible improvements after MVP:

- workout analytics
- progress charts
- progression suggestions
- advanced workout planning
- exercise library
- personal records
- training calendar
- better deployment pipeline
- CI/CD
- public demo environment

---

## Author

Jakub Krzywdziński

- Website: https://exceptionhandled.pl
- LinkedIn: https://pl.linkedin.com/in/jakub-krzywdzi%C5%84ski-a64642332
