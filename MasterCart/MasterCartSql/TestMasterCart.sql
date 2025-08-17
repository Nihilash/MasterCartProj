USE mastercart;

-- ===================================
-- CLEANUP: delete old test data
-- (order matters because of foreign keys)
-- ===================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart_items;
TRUNCATE TABLE carts;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- ===================================
-- SEED DATA
-- ===================================

-- Insert Users
INSERT INTO users (username, password_hash, mobile, gender, age)
VALUES
('Nihilash', 'hash123', '9876543210', 'Male', 22),
('Alice', 'hash234', '9123456780', 'Female', 25),
('Bob', 'hash345', '9988776655', 'Male', 28);

-- Insert Categories
INSERT INTO categories (name, description)
VALUES
('Electronics', 'Gadgets and devices'),
('Books', 'Educational and leisure books'),
('Clothing', 'Apparel and accessories');

-- Insert Products
INSERT INTO products (category_id, name, price_inr, stock_qty)
VALUES
(1, 'Smartphone', 15000.00, 50),
(1, 'Laptop', 55000.00, 20),
(1, 'Earbuds', 1999.00, 100),
(2, 'Data Structures Book', 499.00, 200),
(2, 'Algorithms Book', 699.00, 150),
(3, 'T-Shirt', 799.00, 80),
(3, 'Jeans', 1499.00, 60);

-- Insert Cart for Nihilash
INSERT INTO carts (user_id, status)
VALUES ((SELECT id FROM users WHERE username='Nihilash'), 'ACTIVE');

-- Add items to Nihilash’s cart
INSERT INTO cart_items (cart_id, product_id, quantity, unit_price_inr)
VALUES
((SELECT c.id FROM carts c JOIN users u ON u.id=c.user_id WHERE u.username='Nihilash' AND c.status='ACTIVE'),
 (SELECT id FROM products WHERE name='Smartphone'), 1, 15000.00),
((SELECT c.id FROM carts c JOIN users u ON u.id=c.user_id WHERE u.username='Nihilash' AND c.status='ACTIVE'),
 (SELECT id FROM products WHERE name='Data Structures Book'), 2, 499.00);

-- ===================================
-- TEST QUERIES
-- ===================================

-- 1. Show all users
SELECT id, username, mobile, gender, age FROM users;

-- 2. Show categories & products
SELECT c.name AS category, p.name AS product, p.price_inr, p.stock_qty
FROM products p
JOIN categories c ON c.id = p.category_id
ORDER BY c.name, p.name;

-- 3. Show Nihilash’s active cart
SELECT u.username, ci.cart_id, p.name AS product, ci.quantity, ci.unit_price_inr,
       (ci.quantity * ci.unit_price_inr) AS line_total
FROM cart_items ci
JOIN carts ca ON ca.id = ci.cart_id
JOIN users u ON u.id = ca.user_id
JOIN products p ON p.id = ci.product_id
WHERE u.username = 'Nihilash' AND ca.status = 'ACTIVE';

-- 4. Place a fake order for testing
INSERT INTO orders (user_id, cart_id, subtotal_inr, gst_amount_inr, total_inr)
SELECT ca.user_id, ca.id,
       SUM(ci.quantity * ci.unit_price_inr) AS subtotal,
       SUM(ci.quantity * ci.unit_price_inr) * 0.18 AS gst,
       SUM(ci.quantity * ci.unit_price_inr) * 1.18 AS total
FROM carts ca
JOIN cart_items ci ON ci.cart_id = ca.id
JOIN users u ON u.id = ca.user_id
WHERE u.username = 'Nihilash' AND ca.status='ACTIVE'
GROUP BY ca.id;

-- Copy items into order_items (snapshot)
INSERT INTO order_items (order_id, product_id, product_name_snap, unit_price_inr, quantity, line_total_inr)
SELECT o.id, p.id, p.name, ci.unit_price_inr, ci.quantity, (ci.quantity * ci.unit_price_inr)
FROM orders o
JOIN carts ca ON ca.id=o.cart_id
JOIN cart_items ci ON ci.cart_id=ca.id
JOIN products p ON p.id=ci.product_id
WHERE o.user_id=(SELECT id FROM users WHERE username='Nihilash')
ORDER BY ci.id;

-- 5. View invoice
SELECT * FROM invoice_view WHERE username='Nihilash';

-- Get the last 5 entries in the cart/orders table
SELECT * 
FROM  orders
ORDER BY id ;
