# Project Architecture

Spring Boot REST API for a fashion e-commerce platform.

## Stack

Spring Boot, Spring Security + JWT, JPA, Flyway, MySQL 8.4, Redis, MinIO, and Nginx.

## Run

```bash
docker compose up --build
```

## Local URLs

- API: `http://localhost/api/v1`
- Swagger UI: `http://localhost/swagger-ui/index.html`
- phpMyAdmin: `http://localhost:8081`
- MinIO Console: `http://localhost:9001`

## Cloudflare Tunnel

With Docker Compose running, start the named tunnel:

```sh
cloudflared tunnel --config cloudflare/config.yml run avenzo
```

See [Cloudflare Tunnel setup](doc/run-with-cloudflare.md) for DNS, validation, and troubleshooting.

## Documentation

- API endpoints: [`doc/endpoint.md`](doc/endpoint.md)
- Database schema: [`doc/database.md`](doc/database.md)
- Architecture: [`doc/architecture.md`](doc/architecture.md)
