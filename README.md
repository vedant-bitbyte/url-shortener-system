# Scalable URL Shortener Backend

REST API for shortening URLs, JWT-secured management endpoints, public redirects, and click analytics. Spring Boot 3 · Java 21 · MySQL · Redis · Docker.

---

## Features

- User registration & login (BCrypt)
- JWT auth on protected routes
- 6-character short codes + public redirects
- Click analytics (`clickCount`, `lastAccessed`)
- Redis cache-aside on redirects
- Global exception handling (404, validation)
- Docker Compose (app, MySQL, Redis)

---

## Tech Stack

Spring Boot · Java 21 · Maven · MySQL 8 · Redis 7 · Spring Security · JJWT · JPA · Docker

---

## API Endpoints

Base: `http://localhost:8080`

| Method | Endpoint | Auth |
|--------|----------|------|
| `POST` | `/api/auth/register` | Public |
| `POST` | `/api/auth/login` | Public |
| `POST` | `/api/url/shorten` | JWT |
| `GET` | `/api/url/analytics/{shortCode}` | JWT |
| `GET` | `/{shortCode}` | Public |
| `GET` | `/api/health` | JWT |
| `GET` | `/api/test` | JWT |

---

## Auth Flow

1. Register → `POST /api/auth/register`
2. Login → `POST /api/auth/login` → receive `{ "token": "..." }`
3. Protected calls → `Authorization: Bearer <token>`

Stateless JWT (HS256, 24h expiry). `JwtAuthenticationFilter` validates each request.

---

## Redis (Cache-Aside)

On redirect: read `url:{shortCode}` from Redis → on miss, load MySQL and cache → always update `clickCount` / `lastAccessed` in MySQL.

---

## Project Structure

```
src/main/java/com/vedant/urlshortener/
├── config/          # Security, Redis
├── controller/      # Auth, URL, Redirect, Health
├── dto/
├── exception/
├── model/
├── repository/
├── security/        # JWT, filters
├── service/
└── util/
```

---

## Quick Start

### Clone & configure

```bash
git clone <your-repo-url>
cd urlshortener
cp .env.example .env
# Set your local secrets in .env (not committed)
```

### Docker Compose

```bash
mvn clean package -DskipTests
docker compose up --build
```

| Service | Port |
|---------|------|
| API | 8080 |
| MySQL | 3307 |
| Redis | 6379 |

### Local run (app only)

```bash
docker compose up mysql redis -d
mvn spring-boot:run
```

### Maven

```bash
mvn clean package -DskipTests   # build JAR for Docker
mvn spring-boot:run             # dev
```

---

## Example Requests

**Login**

```http
POST /api/auth/login
Content-Type: application/json

{ "email": "user@example.com", "password": "secure12" }
```

```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

**Shorten** (with Bearer token)

```http
POST /api/url/shorten

{ "originalUrl": "https://example.com/page" }
```

→ `http://localhost:8080/aB3xY9`

**Redirect** (no auth)

```http
GET /aB3xY9
```

→ `302` to original URL

---

## Postman

1. Login → save `token` to environment (`pm.environment.set("token", pm.response.json().token)`).
2. Collection auth: Bearer `{{token}}`.
3. Test shorten, analytics, then redirect without auth.

---

## Roadmap

Rate limiting · custom aliases · Swagger · cache TTL · CI/CD

---

Backend portfolio project by **Vedant Pawar** — secure APIs, persistence, caching, and containerized dependencies in one codebase.
