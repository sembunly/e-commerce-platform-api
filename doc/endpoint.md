# REST API

Planned contract; implementation not verified. Base URL: `http://localhost/api/v1` (Nginx). Direct development: `http://localhost:8080/api/v1` when running locally.

## Authentication

| Method | Path |
|---|---|
| POST | `/auth/register` |
| POST | `/auth/login` |
| POST | `/auth/logout` |
| POST | `/auth/refresh` |
| POST | `/auth/forgot-password` |
| POST | `/auth/reset-password` |
| GET | `/auth/me` |

Implemented request summary:

- `register`: `username`, `email`, `password` (minimum 8 characters); optional `firstName`, `lastName`, `phone`.
- `login`: `username`, `password`; returns a 15-minute Bearer access token and a 30-day refresh token.
- `refresh` and `logout`: `{ "refreshToken": "..." }`. Refresh tokens are stored only as SHA-256 hashes and rotated on refresh.
- `forgot-password`: `{ "email": "..." }`; returns the same response whether or not the account exists. Configure an email provider before production use.
- `reset-password`: `{ "token": "...", "password": "..." }`.
- `me`: send `Authorization: Bearer <accessToken>`.

## Navigation and homepage

| Method | Path |
|---|---|
| GET | `/navigation` |
| GET | `/navigation/{slug}` |
| GET | `/categories` |
| GET | `/categories/{slug}` |
| GET | `/categories/{slug}/children` |
| GET | `/collections` |
| GET | `/collections/{slug}` |
| GET | `/home` |
| GET | `/banners` |
| GET | `/products/new-arrivals` |
| GET | `/products/featured` |
| GET | `/products/best-sellers` |
| GET | `/products/sale` |

## Catalog

| Method | Path |
|---|---|
| GET | `/products` |
| GET | `/products/{slug}` |
| GET | `/products/{slug}/variants` |
| GET | `/products/{slug}/related` |
| GET | `/products/search` |
| GET | `/products/filters` |
| GET | `/categories/{slug}/products` |
| GET | `/collections/{slug}/products` |
| GET | `/stores` |
| GET | `/stores/{id}` |
| GET | `/articles` |
| GET | `/articles/{slug}` |

## Cart and wishlist

| Method | Path |
|---|---|
| GET | `/cart` |
| GET | `/wishlist` |
| POST | `/cart/items` |
| POST | `/cart/coupon` |
| POST | `/wishlist/items` |
| PATCH | `/cart/items/{id}` |
| DELETE | `/cart/items/{id}` |
| DELETE | `/cart/items` |
| DELETE | `/cart/coupon` |
| DELETE | `/wishlist/items/{id}` |

## Account

| Method | Path |
|---|---|
| GET | `/account/profile` |
| GET | `/account/addresses` |
| POST | `/account/addresses` |
| PUT | `/account/profile` |
| PUT | `/account/addresses/{id}` |
| PATCH | `/account/password` |
| DELETE | `/account/addresses/{id}` |

## Orders and payment

| Method | Path |
|---|---|
| GET | `/orders` |
| GET | `/orders/{orderNumber}` |
| GET | `/orders/{orderNumber}/tracking` |
| GET | `/payment-methods` |
| GET | `/payments/{id}/status` |
| POST | `/checkout/preview` |
| POST | `/orders` |
| POST | `/orders/{orderNumber}/cancel` |
| POST | `/payments` |
| POST | `/payments/webhook/{provider}` |

## Admin

All paths below are relative to `/api/v1/admin`. CRUD = GET/POST collection and GET/PUT/DELETE by ID.

| Method | Path |
|---|---|
| CRUD | `/navigation` |
| CRUD | `/categories` |
| CRUD | `/collections` |
| CRUD | `/products` |
| CRUD | `/products/{id}/variants` |
| CRUD | `/products/{id}/images` |
| CRUD | `/sizes` |
| CRUD | `/colors` |
| CRUD | `/coupons` |
| CRUD | `/banners` |
| CRUD | `/stores` |
| CRUD | `/articles` |
| CRUD | `/users` |
| CRUD | `/roles` |
| GET | `/dashboard` |
| GET | `/inventory` |
| GET | `/orders` |
| GET | `/orders/{id}` |
| GET | `/payments` |
| GET | `/shipments` |
| GET | `/customers` |
| GET | `/permissions` |
| GET | `/reports/sales` |
| GET | `/reports/inventory` |
| PATCH | `/inventory/{id}` |
| PATCH | `/orders/{id}/status` |
| PATCH | `/shipments/{id}` |
| PATCH | `/customers/{id}/status` |
| POST | `/shipments` |
| POST | `/media/upload` |
| PUT | `/roles/{id}/permissions` |
| DELETE | `/media/{id}` |

## Conventions

- Product filters: gender, category, collection, color, size, minPrice, maxPrice, page (zero-based), sizePerPage, sort, keyword.
- JSON DTOs; multipart media upload; Bearer JWT for protected endpoints. Public catalog reads and auth entry points; customer ownership checks; admin authorities from module_detail. Payment webhook requires provider verification.
- HTTP 200, 201, 204, 400, 401, 403, 404, 409, 422, 429, 500 as appropriate.
- Soft delete master data; retain order/payment history.
- Internal health endpoint: `/actuator/health` (not currently proxied by Nginx).
- Proposed extension: expose module_type/module/module_detail management endpoints if dynamic module editing is needed; existing role permission assignment uses module detail IDs.
