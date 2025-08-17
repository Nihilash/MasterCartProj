-- =========================================
-- MasterCart - Database & Schema
-- Target: MySQL 8+
-- =========================================

-- Clean start
DROP DATABASE IF EXISTS mastercart;
CREATE DATABASE mastercart CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE mastercart;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- Table: users
-- =========================
CREATE TABLE users (
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    username        VARCHAR(50) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    mobile          VARCHAR(15),
    gender          ENUM('Male','Female','Other') DEFAULT NULL,
    age             INT CHECK (age >= 0),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB;
-- select*from users;
-- =========================
-- Table: categories
-- =========================
CREATE TABLE categories (
    id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_categories_name (name)
) ENGINE=InnoDB;
-- select*from categories;
-- =========================
-- Table: products
-- =========================
CREATE TABLE products (
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    category_id     INT UNSIGNED NOT NULL,
    name            VARCHAR(100) NOT NULL,
    price_inr       DECIMAL(10,2) NOT NULL,
    stock_qty       INT NOT NULL DEFAULT 0,
    is_active       TINYINT(1) NOT NULL DEFAULT 1,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_products_category (category_id),
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
select*from products;
-- =========================
-- Table: carts
-- =========================
CREATE TABLE carts (
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id         BIGINT UNSIGNED NOT NULL,
    status          ENUM('ACTIVE','CHECKED_OUT','CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_carts_user (user_id),
    CONSTRAINT fk_carts_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;
 -- select*from carts;
-- =========================
-- Table: cart_items
-- =========================
CREATE TABLE cart_items (
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    cart_id         BIGINT UNSIGNED NOT NULL,
    product_id      BIGINT UNSIGNED NOT NULL,
    quantity        INT NOT NULL CHECK (quantity > 0),
    unit_price_inr  DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_cart_product (cart_id, product_id),
    KEY idx_cart_items_cart (cart_id),
    KEY idx_cart_items_product (product_id),
    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id) REFERENCES carts(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
-- select*from cart_items;
-- =========================
-- Table: orders
-- =========================
CREATE TABLE orders (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id             BIGINT UNSIGNED NOT NULL,
    cart_id             BIGINT UNSIGNED,
    subtotal_inr        DECIMAL(12,2) NOT NULL,
    gst_rate            DECIMAL(5,2) NOT NULL DEFAULT 18.00,
    gst_amount_inr      DECIMAL(12,2) NOT NULL,
    total_inr           DECIMAL(12,2) NOT NULL,
    status              ENUM('PLACED','CANCELLED','REFUNDED') NOT NULL DEFAULT 'PLACED',
    placed_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_orders_user (user_id),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
-- select*from orders;
-- =========================
-- Table: order_items
-- =========================
CREATE TABLE order_items (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id            BIGINT UNSIGNED NOT NULL,
    product_id          BIGINT UNSIGNED NOT NULL,
    product_name_snap   VARCHAR(100) NOT NULL,
    unit_price_inr      DECIMAL(10,2) NOT NULL,
    quantity            INT NOT NULL CHECK (quantity > 0),
    line_total_inr      DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_order_items_order (order_id),
    KEY idx_order_items_product (product_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
select*from order_items;
-- =========================
-- View: invoice_view
-- =========================
CREATE OR REPLACE VIEW invoice_view AS
SELECT 
    o.id AS order_id,
    o.placed_at,
    u.username,
    oi.product_name_snap AS product_name,
    oi.quantity,
    oi.unit_price_inr,
    oi.line_total_inr,
    o.subtotal_inr,
    o.gst_rate,
    o.gst_amount_inr,
    o.total_inr
FROM orders o
JOIN users u ON u.id = o.user_id
JOIN order_items oi ON oi.order_id = o.id;

SET FOREIGN_KEY_CHECKS = 1;

