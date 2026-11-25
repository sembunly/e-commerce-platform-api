# Docker Development

Run from the project root with Docker Desktop and Compose v2.

| Service | Internal address | Host access |
|---|---|---|
| nginx | nginx:80 | http://localhost |
| api | api:8080 | /api/v1 via Nginx |
| mysql | mysql:3306 | 127.0.0.1:3307 |
| redis | redis:6379 | Internal only |
| minio | minio:9000 | 127.0.0.1:9000, console 9001 if supported |
| phpmyadmin | phpmyadmin:80 | http://localhost:8081 |

## Configuration

Use `.env` (gitignored): MYSQL_DATABASE=fashion_ecom_db, MYSQL_USER=fashion_user, MYSQL_PASSWORD, MYSQL_ROOT_PASSWORD, REDIS_PASSWORD, MINIO_ROOT_USER, MINIO_ROOT_PASSWORD, APP_PORT=80. Use a MinIO password of at least eight characters. Never commit real secrets. Compose passes Spring datasource, Redis and MinIO environment variables. Nginx proxies /api/ to api:8080 preserving the path.

```sh
docker compose config --quiet
docker compose up -d --build
docker compose ps
docker compose logs -f api
```

## Database access

phpMyAdmin uses PMA_HOST=mysql and PMA_PORT=3306. Login with MYSQL_USER and MYSQL_PASSWORD.

```sh
docker compose exec mysql mysql -u fashion_user -p fashion_ecom_db
```

```sql
SHOW TABLES;
SELECT * FROM flyway_schema_history;
```

## Troubleshooting

- Existing MySQL volumes retain old credentials; changing .env does not reset them.
- MinIO image pull previously failed. Verify the replacement image, entrypoint, permissions and console support before deploying; availability is not confirmed here.
- Check Maven parent version availability and API logs if builds fail.
- `docker compose down` preserves volumes. `docker compose down -v` permanently deletes project database, cache and object storage; use only for disposable development data after backup.
- The merged migration requires a fresh schema or a deliberate Flyway migration strategy. Do not reset production.
