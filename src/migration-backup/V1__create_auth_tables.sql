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
);