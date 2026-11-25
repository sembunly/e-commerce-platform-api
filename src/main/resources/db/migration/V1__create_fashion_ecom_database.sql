-- =====================================================
-- V1: Authentication and Authorization
-- Database: MySQL 8.4
-- =====================================================

-- 01. Users
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       phone VARCHAR(30),
                       avatar_url VARCHAR(500),
                       email_verified TINYINT NOT NULL DEFAULT 0,
                       last_login_at DATETIME NULL,
                       is_active TINYINT NOT NULL DEFAULT 1,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CHECK (is_active IN (0, 1))
);

-- 02. Roles
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       code VARCHAR(100) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       is_system TINYINT NOT NULL DEFAULT 0,
                       is_active TINYINT NOT NULL DEFAULT 1,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CHECK (is_active IN (0, 1))
);

-- 03. User Roles
CREATE TABLE user_roles (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            is_active TINYINT NOT NULL DEFAULT 1,
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            UNIQUE (user_id, role_id),
                            CHECK (is_active IN (0, 1))
);

-- 04. Module Type
CREATE TABLE module_type (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             code VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255),
                             icon VARCHAR(100),
                             sort_order INT NOT NULL DEFAULT 0,
                             is_active TINYINT NOT NULL DEFAULT 1,
                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP,

                             CHECK (is_active IN (0, 1))
);

-- 05. Module
CREATE TABLE module (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        module_type_id BIGINT NOT NULL,
                        parent_id BIGINT NULL,
                        name VARCHAR(100) NOT NULL,
                        code VARCHAR(100) NOT NULL UNIQUE,
                        route VARCHAR(255),
                        icon VARCHAR(100),
                        description VARCHAR(255),
                        sort_order INT NOT NULL DEFAULT 0,
                        is_visible TINYINT NOT NULL DEFAULT 1,
                        is_active TINYINT NOT NULL DEFAULT 1,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

                        CHECK (is_active IN (0, 1))
);

-- 06. Module Detail
CREATE TABLE module_detail (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               module_id BIGINT NOT NULL,
                               name VARCHAR(100) NOT NULL,
                               code VARCHAR(150) NOT NULL UNIQUE,
                               action VARCHAR(50) NOT NULL,
                               description VARCHAR(255),
                               sort_order INT NOT NULL DEFAULT 0,
                               is_active TINYINT NOT NULL DEFAULT 1,
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP,

                               CHECK (is_active IN (0, 1))
);

-- 07. Role Permissions
CREATE TABLE role_permissions (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  role_id BIGINT NOT NULL,
                                  module_detail_id BIGINT NOT NULL,
                                  is_active TINYINT NOT NULL DEFAULT 1,
                                  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,

                                  UNIQUE (role_id, module_detail_id),
                                  CHECK (is_active IN (0, 1))
);

-- 08. Authentication Tokens
CREATE TABLE auth_tokens (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             token_hash VARCHAR(255) NOT NULL UNIQUE,
                             token_type VARCHAR(30) NOT NULL,
                             expires_at DATETIME NOT NULL,
                             revoked_at DATETIME NULL,
                             used_at DATETIME NULL,
                             ip_address VARCHAR(45),
                             user_agent VARCHAR(500),
                             is_active TINYINT NOT NULL DEFAULT 1,
                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CHECK (is_active IN (0, 1)),
                             CHECK (
                                 token_type IN (
                                                'REFRESH',
                                                'PASSWORD_RESET',
                                                'EMAIL_VERIFICATION'
                                     )
                                 )
);CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NULL,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(250) NOT NULL UNIQUE,
    description TEXT,
    image_id BIGINT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(250) NOT NULL UNIQUE,
    description TEXT,
    image_id BIGINT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE navigation_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NULL,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(200) NOT NULL UNIQUE,
    category_id BIGINT NULL,
    collection_id BIGINT NULL,
    url VARCHAR(500),
    sort_order INT NOT NULL DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    object_key VARCHAR(500) NOT NULL UNIQUE,
    bucket VARCHAR(100) NOT NULL,
    content_type VARCHAR(100),
    size_bytes BIGINT NOT NULL CHECK (size_bytes >= 0),
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sizes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    sort_order INT NOT NULL DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE colors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    hex_code CHAR(7) NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(250) NOT NULL UNIQUE,
    description TEXT,
    base_price DECIMAL(12,2) NOT NULL CHECK (base_price >= 0),
    gender VARCHAR(20),
    material VARCHAR(150),
    is_featured TINYINT NOT NULL DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (product_id, category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product_collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    collection_id BIGINT NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (product_id, collection_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product_variants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    size_id BIGINT NULL,
    color_id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    price DECIMAL(12,2) NOT NULL CHECK (price >= 0),
    sale_price DECIMAL(12,2) NULL CHECK (sale_price >= 0 AND sale_price <= price),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    reserved_quantity INT NOT NULL DEFAULT 0 CHECK (reserved_quantity >= 0 AND reserved_quantity <= stock_quantity),
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (product_id, color_id, size_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    color_id BIGINT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    alt_text VARCHAR(255),
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    coupon_id BIGINT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    variant_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (cart_id, variant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE wishlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    discount_type VARCHAR(20) NOT NULL CHECK (discount_type IN ('FIXED','PERCENT')),
    discount_value DECIMAL(12,2) NOT NULL CHECK (discount_value > 0),
    min_order_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    usage_limit INT NULL,
    starts_at DATETIME NOT NULL,
    ends_at DATETIME NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (ends_at > starts_at AND (discount_type <> 'PERCENT' OR discount_value <= 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE banners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    media_id BIGINT NOT NULL,
    target_url VARCHAR(500),
    sort_order INT NOT NULL DEFAULT 0,
    starts_at DATETIME NULL,
    ends_at DATETIME NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE stores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(500) NOT NULL,
    phone VARCHAR(30),
    opening_hours VARCHAR(255),
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(250) NOT NULL UNIQUE,
    content LONGTEXT NOT NULL,
    media_id BIGINT NULL,
    author_id BIGINT NULL,
    published_at DATETIME NULL,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recipient_name VARCHAR(200) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    address_line VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'Cambodia',
    is_default TINYINT NOT NULL DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)), created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NULL,
    order_number VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','CONFIRMED','PROCESSING','SHIPPED','DELIVERED','CANCELLED')),
    shipping_address JSON NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL CHECK (subtotal >= 0),
    discount_amount DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (discount_amount >= 0),
    shipping_fee DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (shipping_fee >= 0),
    total_amount DECIMAL(12,2) NOT NULL CHECK (total_amount >= 0),
    currency CHAR(3) NOT NULL DEFAULT 'USD',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (total_amount = subtotal - discount_amount + shipping_fee)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    variant_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    sku VARCHAR(100) NOT NULL,
    size_name VARCHAR(50),
    color_name VARCHAR(100),
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(12,2) NOT NULL CHECK (unit_price >= 0),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE shipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    carrier VARCHAR(100),
    tracking_number VARCHAR(150),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','SHIPPED','DELIVERED','FAILED','RETURNED')),
    shipped_at DATETIME NULL,
    delivered_at DATETIME NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    method VARCHAR(30) NOT NULL CHECK (method IN ('COD','KHQR')),
    provider VARCHAR(100),
    provider_reference VARCHAR(200) NULL,
    idempotency_key VARCHAR(150) NOT NULL UNIQUE,
    amount DECIMAL(12,2) NOT NULL CHECK (amount >= 0),
    currency CHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','PAID','FAILED','REFUNDED','EXPIRED')),
    paid_at DATETIME NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (provider, provider_reference)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inventory_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    variant_id BIGINT NOT NULL,
    order_id BIGINT NULL,
    created_by BIGINT NULL,
    movement_type VARCHAR(30) NOT NULL CHECK (movement_type IN ('IN','OUT','ADJUST','RETURN')),
    quantity_change INT NOT NULL CHECK (quantity_change <> 0),
    note VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE categories ADD CONSTRAINT fk_categories_parent_id FOREIGN KEY (parent_id) REFERENCES categories(id);
ALTER TABLE categories ADD CONSTRAINT fk_categories_image_id FOREIGN KEY (image_id) REFERENCES media(id);
ALTER TABLE collections ADD CONSTRAINT fk_collections_image_id FOREIGN KEY (image_id) REFERENCES media(id);
ALTER TABLE navigation_items ADD CONSTRAINT fk_navigation_items_parent_id FOREIGN KEY (parent_id) REFERENCES navigation_items(id);
ALTER TABLE navigation_items ADD CONSTRAINT fk_navigation_items_category_id FOREIGN KEY (category_id) REFERENCES categories(id);
ALTER TABLE navigation_items ADD CONSTRAINT fk_navigation_items_collection_id FOREIGN KEY (collection_id) REFERENCES collections(id);
ALTER TABLE product_categories ADD CONSTRAINT fk_product_categories_product_id FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE product_categories ADD CONSTRAINT fk_product_categories_category_id FOREIGN KEY (category_id) REFERENCES categories(id);
ALTER TABLE product_collections ADD CONSTRAINT fk_product_collections_product_id FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE product_collections ADD CONSTRAINT fk_product_collections_collection_id FOREIGN KEY (collection_id) REFERENCES collections(id);
ALTER TABLE product_variants ADD CONSTRAINT fk_product_variants_product_id FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE product_variants ADD CONSTRAINT fk_product_variants_size_id FOREIGN KEY (size_id) REFERENCES sizes(id);
ALTER TABLE product_variants ADD CONSTRAINT fk_product_variants_color_id FOREIGN KEY (color_id) REFERENCES colors(id);
ALTER TABLE product_images ADD CONSTRAINT fk_product_images_product_id FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE product_images ADD CONSTRAINT fk_product_images_media_id FOREIGN KEY (media_id) REFERENCES media(id);
ALTER TABLE product_images ADD CONSTRAINT fk_product_images_color_id FOREIGN KEY (color_id) REFERENCES colors(id);
ALTER TABLE carts ADD CONSTRAINT fk_carts_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE carts ADD CONSTRAINT fk_carts_coupon_id FOREIGN KEY (coupon_id) REFERENCES coupons(id);
ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_cart_id FOREIGN KEY (cart_id) REFERENCES carts(id);
ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(id);
ALTER TABLE wishlists ADD CONSTRAINT fk_wishlists_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE wishlists ADD CONSTRAINT fk_wishlists_product_id FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE banners ADD CONSTRAINT fk_banners_media_id FOREIGN KEY (media_id) REFERENCES media(id);
ALTER TABLE articles ADD CONSTRAINT fk_articles_media_id FOREIGN KEY (media_id) REFERENCES media(id);
ALTER TABLE articles ADD CONSTRAINT fk_articles_author_id FOREIGN KEY (author_id) REFERENCES users(id);
ALTER TABLE addresses ADD CONSTRAINT fk_addresses_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_coupon_id FOREIGN KEY (coupon_id) REFERENCES coupons(id);
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(id);
ALTER TABLE shipments ADD CONSTRAINT fk_shipments_order_id FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE payments ADD CONSTRAINT fk_payments_order_id FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE inventory_movements ADD CONSTRAINT fk_inventory_movements_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(id);
ALTER TABLE inventory_movements ADD CONSTRAINT fk_inventory_movements_order_id FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE inventory_movements ADD CONSTRAINT fk_inventory_movements_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles(id);
ALTER TABLE module ADD CONSTRAINT fk_module_module_type_id FOREIGN KEY (module_type_id) REFERENCES module_type(id);
ALTER TABLE module ADD CONSTRAINT fk_module_parent_id FOREIGN KEY (parent_id) REFERENCES module(id);
ALTER TABLE module_detail ADD CONSTRAINT fk_module_detail_module_id FOREIGN KEY (module_id) REFERENCES module(id);
ALTER TABLE role_permissions ADD CONSTRAINT fk_role_permissions_role_id FOREIGN KEY (role_id) REFERENCES roles(id);
ALTER TABLE role_permissions ADD CONSTRAINT fk_role_permissions_module_detail_id FOREIGN KEY (module_detail_id) REFERENCES module_detail(id);
ALTER TABLE auth_tokens ADD CONSTRAINT fk_auth_tokens_user_id FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_products_active_created ON products(is_active, created_at);
CREATE INDEX idx_orders_user_created ON orders(user_id, created_at);
CREATE INDEX idx_variants_filter ON product_variants(color_id, size_id, is_active);
CREATE INDEX idx_tokens_expiry ON auth_tokens(expires_at);
CREATE INDEX idx_inventory_variant_created ON inventory_movements(variant_id, created_at);
INSERT INTO roles(name,code,is_system) VALUES ('Administrator','ADMIN',1),('Staff','STAFF',0),('Customer','CUSTOMER',1);
INSERT INTO module_type(name,code,sort_order) VALUES ('Overview','OVERVIEW',1),('Catalog','CATALOG',2),('Sales','SALES',3),('Marketing','MARKETING',4),('Settings','SETTINGS',5);
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Dashboard','DASHBOARD','/admin/dashboard',1 FROM module_type WHERE code='OVERVIEW';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Dashboard','DASHBOARD_VIEW','VIEW' FROM module WHERE code='DASHBOARD';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Reports','REPORT','/admin/reports',2 FROM module_type WHERE code='OVERVIEW';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Reports','REPORT_VIEW','VIEW' FROM module WHERE code='REPORT';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Products','PRODUCT','/admin/products',1 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Products','PRODUCT_VIEW','VIEW' FROM module WHERE code='PRODUCT';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Products','PRODUCT_CREATE','CREATE' FROM module WHERE code='PRODUCT';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Products','PRODUCT_UPDATE','UPDATE' FROM module WHERE code='PRODUCT';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Products','PRODUCT_DELETE','DELETE' FROM module WHERE code='PRODUCT';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Categories','CATEGORY','/admin/categories',2 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Categories','CATEGORY_VIEW','VIEW' FROM module WHERE code='CATEGORY';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Categories','CATEGORY_CREATE','CREATE' FROM module WHERE code='CATEGORY';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Categories','CATEGORY_UPDATE','UPDATE' FROM module WHERE code='CATEGORY';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Categories','CATEGORY_DELETE','DELETE' FROM module WHERE code='CATEGORY';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Collections','COLLECTION','/admin/collections',3 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Collections','COLLECTION_VIEW','VIEW' FROM module WHERE code='COLLECTION';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Collections','COLLECTION_CREATE','CREATE' FROM module WHERE code='COLLECTION';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Collections','COLLECTION_UPDATE','UPDATE' FROM module WHERE code='COLLECTION';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Collections','COLLECTION_DELETE','DELETE' FROM module WHERE code='COLLECTION';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Inventory','INVENTORY','/admin/inventory',4 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Inventory','INVENTORY_VIEW','VIEW' FROM module WHERE code='INVENTORY';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Adjust Inventory','INVENTORY_ADJUST','ADJUST' FROM module WHERE code='INVENTORY';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Sizes','SIZE','/admin/sizes',5 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Sizes','SIZE_VIEW','VIEW' FROM module WHERE code='SIZE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Sizes','SIZE_CREATE','CREATE' FROM module WHERE code='SIZE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Sizes','SIZE_UPDATE','UPDATE' FROM module WHERE code='SIZE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Sizes','SIZE_DELETE','DELETE' FROM module WHERE code='SIZE';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Colors','COLOR','/admin/colors',6 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Colors','COLOR_VIEW','VIEW' FROM module WHERE code='COLOR';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Colors','COLOR_CREATE','CREATE' FROM module WHERE code='COLOR';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Colors','COLOR_UPDATE','UPDATE' FROM module WHERE code='COLOR';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Colors','COLOR_DELETE','DELETE' FROM module WHERE code='COLOR';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Media','MEDIA','/admin/media',7 FROM module_type WHERE code='CATALOG';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Media','MEDIA_VIEW','VIEW' FROM module WHERE code='MEDIA';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Media','MEDIA_CREATE','CREATE' FROM module WHERE code='MEDIA';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Media','MEDIA_UPDATE','UPDATE' FROM module WHERE code='MEDIA';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Media','MEDIA_DELETE','DELETE' FROM module WHERE code='MEDIA';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Orders','ORDER','/admin/orders',1 FROM module_type WHERE code='SALES';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Orders','ORDER_VIEW','VIEW' FROM module WHERE code='ORDER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Confirm Orders','ORDER_CONFIRM','CONFIRM' FROM module WHERE code='ORDER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Cancel Orders','ORDER_CANCEL','CANCEL' FROM module WHERE code='ORDER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Orders','ORDER_UPDATE','UPDATE' FROM module WHERE code='ORDER';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Payments','PAYMENT','/admin/payments',2 FROM module_type WHERE code='SALES';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Payments','PAYMENT_VIEW','VIEW' FROM module WHERE code='PAYMENT';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Refund Payments','PAYMENT_REFUND','REFUND' FROM module WHERE code='PAYMENT';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Shipments','SHIPMENT','/admin/shipments',3 FROM module_type WHERE code='SALES';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Shipments','SHIPMENT_VIEW','VIEW' FROM module WHERE code='SHIPMENT';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Shipments','SHIPMENT_CREATE','CREATE' FROM module WHERE code='SHIPMENT';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Shipments','SHIPMENT_UPDATE','UPDATE' FROM module WHERE code='SHIPMENT';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Customers','CUSTOMER','/admin/customers',4 FROM module_type WHERE code='SALES';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Customers','CUSTOMER_VIEW','VIEW' FROM module WHERE code='CUSTOMER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Customers','CUSTOMER_UPDATE','UPDATE' FROM module WHERE code='CUSTOMER';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Navigation','NAVIGATION','/admin/navigation',1 FROM module_type WHERE code='MARKETING';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Navigation','NAVIGATION_VIEW','VIEW' FROM module WHERE code='NAVIGATION';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Navigation','NAVIGATION_CREATE','CREATE' FROM module WHERE code='NAVIGATION';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Navigation','NAVIGATION_UPDATE','UPDATE' FROM module WHERE code='NAVIGATION';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Navigation','NAVIGATION_DELETE','DELETE' FROM module WHERE code='NAVIGATION';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Coupons','COUPON','/admin/coupons',2 FROM module_type WHERE code='MARKETING';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Coupons','COUPON_VIEW','VIEW' FROM module WHERE code='COUPON';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Coupons','COUPON_CREATE','CREATE' FROM module WHERE code='COUPON';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Coupons','COUPON_UPDATE','UPDATE' FROM module WHERE code='COUPON';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Coupons','COUPON_DELETE','DELETE' FROM module WHERE code='COUPON';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Banners','BANNER','/admin/banners',3 FROM module_type WHERE code='MARKETING';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Banners','BANNER_VIEW','VIEW' FROM module WHERE code='BANNER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Banners','BANNER_CREATE','CREATE' FROM module WHERE code='BANNER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Banners','BANNER_UPDATE','UPDATE' FROM module WHERE code='BANNER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Banners','BANNER_DELETE','DELETE' FROM module WHERE code='BANNER';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Stores','STORE','/admin/stores',4 FROM module_type WHERE code='MARKETING';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Stores','STORE_VIEW','VIEW' FROM module WHERE code='STORE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Stores','STORE_CREATE','CREATE' FROM module WHERE code='STORE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Stores','STORE_UPDATE','UPDATE' FROM module WHERE code='STORE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Stores','STORE_DELETE','DELETE' FROM module WHERE code='STORE';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Articles','ARTICLE','/admin/articles',5 FROM module_type WHERE code='MARKETING';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Articles','ARTICLE_VIEW','VIEW' FROM module WHERE code='ARTICLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Articles','ARTICLE_CREATE','CREATE' FROM module WHERE code='ARTICLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Articles','ARTICLE_UPDATE','UPDATE' FROM module WHERE code='ARTICLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Articles','ARTICLE_DELETE','DELETE' FROM module WHERE code='ARTICLE';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Users','USER','/admin/users',1 FROM module_type WHERE code='SETTINGS';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Users','USER_VIEW','VIEW' FROM module WHERE code='USER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Users','USER_CREATE','CREATE' FROM module WHERE code='USER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Users','USER_UPDATE','UPDATE' FROM module WHERE code='USER';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Users','USER_DELETE','DELETE' FROM module WHERE code='USER';
INSERT INTO module(module_type_id,name,code,route,sort_order) SELECT id,'Roles','ROLE','/admin/roles',2 FROM module_type WHERE code='SETTINGS';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'View Roles','ROLE_VIEW','VIEW' FROM module WHERE code='ROLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Create Roles','ROLE_CREATE','CREATE' FROM module WHERE code='ROLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Update Roles','ROLE_UPDATE','UPDATE' FROM module WHERE code='ROLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Delete Roles','ROLE_DELETE','DELETE' FROM module WHERE code='ROLE';
INSERT INTO module_detail(module_id,name,code,action) SELECT id,'Manage Roles','ROLE_MANAGE','MANAGE' FROM module WHERE code='ROLE';
INSERT INTO role_permissions(role_id,module_detail_id) SELECT r.id,d.id FROM roles r CROSS JOIN module_detail d WHERE r.code='ADMIN';
INSERT INTO role_permissions(role_id,module_detail_id) SELECT r.id,d.id FROM roles r CROSS JOIN module_detail d WHERE r.code='STAFF' AND d.code IN ('DASHBOARD_VIEW','PRODUCT_VIEW','ORDER_VIEW','INVENTORY_VIEW');
INSERT INTO categories(name,slug) VALUES ('Men','men'),('Women','women');
INSERT INTO categories(parent_id,name,slug) SELECT id,'Shoes',CONCAT(slug,'-shoes') FROM categories WHERE slug IN ('men','women');
INSERT INTO categories(parent_id,name,slug) SELECT id,'Bags',CONCAT(slug,'-bags') FROM categories WHERE slug IN ('men','women');
INSERT INTO navigation_items(name,slug,category_id,sort_order) SELECT name,slug,id,id FROM categories WHERE parent_id IS NULL;
INSERT INTO navigation_items(parent_id,name,slug,category_id) SELECT n.id,c.name,c.slug,c.id FROM categories c JOIN navigation_items n ON n.category_id=c.parent_id;
INSERT INTO sizes(name,sort_order) VALUES ('ONE SIZE',0),('40',40),('41',41),('42',42);
INSERT INTO colors(name,hex_code) VALUES ('Black','#000000'),('Brown','#795037');
INSERT INTO products(name,slug,base_price,gender) VALUES ('Classic Derby Shoes','classic-derby-shoes',89.00,'MEN');
INSERT INTO product_categories(product_id,category_id) SELECT p.id,c.id FROM products p CROSS JOIN categories c WHERE p.slug='classic-derby-shoes' AND c.slug='men-shoes';
INSERT INTO product_variants(product_id,size_id,color_id,sku,price,stock_quantity) SELECT p.id,s.id,c.id,'DS-BK-40',89.00,10 FROM products p CROSS JOIN sizes s CROSS JOIN colors c WHERE p.slug='classic-derby-shoes' AND s.name='40' AND c.name='Black';
INSERT INTO inventory_movements(variant_id,movement_type,quantity_change,note) SELECT id,'IN',10,'Demo opening stock' FROM product_variants WHERE sku='DS-BK-40';