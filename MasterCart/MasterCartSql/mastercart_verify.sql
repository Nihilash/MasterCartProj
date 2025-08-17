USE mastercart;

-- Check all tables exist
SHOW TABLES;

-- Count basics (fixed alias issue)
SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users
UNION ALL
SELECT 'categories', COUNT(*) FROM categories
UNION ALL
SELECT 'products', COUNT(*) FROM products
UNION ALL
SELECT 'carts', COUNT(*) FROM carts
UNION ALL
SELECT 'cart_items', COUNT(*) FROM cart_items;

-- Peek users (only id & username exist in Phase 1)
SELECT id, username FROM users;

-- Peek products with categories
SELECT c.name AS category, p.name AS product, p.price
FROM products p
JOIN categories c ON c.id = p.category_id
ORDER BY c.name, p.name;

-- Sample cart for a given user (replace 'john' / 'alice' as per inserted data)
SELECT u.username, ci.cart_id, p.name AS product, ci.quantity, p.price,
       (ci.quantity * p.price) AS line_total
FROM cart_items ci
JOIN carts ca ON ca.id = ci.cart_id
JOIN users u ON u.id = ca.user_id
JOIN products p ON p.id = ci.product_id
WHERE u.username = 'john';

-- Orders, order_items & invoice_view do NOT exist in Phase 1
-- Keep placeholders for Phase 3 (Order + Invoice system)
-- SELECT * FROM orders LIMIT 5;
-- SELECT * FROM order_items LIMIT 5;
-- SELECT * FROM invoice_view LIMIT 1;
