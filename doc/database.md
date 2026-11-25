# Database

MySQL 8.4 · `fashion_ecom_db` · InnoDB · utf8mb4.

Single Flyway migration: `src/main/resources/db/migration/V1__create_fashion_ecom_database.sql`. This is the planned concatenation of V1–V10, not a verified deployed schema.

## Tables (32)

| Table | Business columns (excluding ID and common audit fields) |
|---|---|
| `users` | username, email, password_hash, first_name, last_name, phone, avatar_url, email_verified, last_login_at |
| `roles` | name, code, description, is_system |
| `user_roles` | user_id, role_id |
| `module_type` | name, code, description, icon, sort_order |
| `module` | module_type_id, parent_id, name, code, route, icon, description, sort_order, is_visible |
| `module_detail` | module_id, name, code, action, description, sort_order |
| `role_permissions` | role_id, module_detail_id |
| `auth_tokens` | user_id, token_hash, token_type, expires_at, revoked_at, used_at, ip_address, user_agent |
| `categories` | parent_id, name, slug, description, image_id, sort_order |
| `collections` | name, slug, description, image_id |
| `navigation_items` | parent_id, name, slug, category_id, collection_id, url, sort_order |
| `media` | object_key, bucket, content_type, size_bytes |
| `sizes` | name, sort_order |
| `colors` | name, hex_code |
| `products` | name, slug, description, base_price, gender, material, is_featured |
| `product_categories` | product_id, category_id |
| `product_collections` | product_id, collection_id |
| `product_variants` | product_id, size_id, color_id, sku, price, sale_price, stock_quantity, reserved_quantity |
| `product_images` | product_id, media_id, color_id, sort_order, alt_text |
| `carts` | user_id, coupon_id |
| `cart_items` | cart_id, variant_id, quantity |
| `wishlists` | user_id, product_id |
| `coupons` | code, discount_type, discount_value, min_order_amount, usage_limit, starts_at, ends_at |
| `banners` | title, media_id, target_url, sort_order, starts_at, ends_at |
| `stores` | name, address, phone, opening_hours, latitude, longitude |
| `articles` | name, slug, content, media_id, author_id, published_at |
| `addresses` | user_id, recipient_name, phone, address_line, city, country, is_default |
| `orders` | user_id, coupon_id, order_number, status, shipping_address, subtotal, discount_amount, shipping_fee, total_amount, currency |
| `order_items` | order_id, variant_id, product_name, sku, size_name, color_name, quantity, unit_price |
| `shipments` | order_id, carrier, tracking_number, status, shipped_at, delivered_at |
| `payments` | order_id, method, provider, provider_reference, idempotency_key, amount, currency, status, paid_at |
| `inventory_movements` | variant_id, order_id, created_by, movement_type, quantity_change, note |

## Rules

- BIGINT auto-increment primary keys; DECIMAL(12,2) for money; USD default for orders and payments.
- Active master data uses `is_active`; reactivate existing unique mapping rows rather than inserting duplicates. Transaction history is retained.
- Product variants have unique SKU and stock/reserved quantities. MySQL permits multiple NULL sizes in the composite unique key; enforce one-size uniqueness in the service or a later migration.
- Orders retain shipping JSON and item name/SKU/price snapshots.
- Payment idempotency keys are unique; verify provider callbacks.
- Coupon usage limits and stock concurrency require service-level transactions.
- Seed roles ADMIN, STAFF and CUSTOMER; provision admin securely, not through a shared password.
- Hibernate ddl-auto: validate. Never edit an applied Flyway migration in a shared database.
