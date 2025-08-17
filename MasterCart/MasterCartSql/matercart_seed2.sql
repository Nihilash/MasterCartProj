USE mastercart;

-- Truncate child tables first to avoid foreign key issues
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE cart_items;
TRUNCATE TABLE carts;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- Seed users
INSERT INTO users (username, password_hash, mobile, gender, age)
VALUES
('NihilashL', 'Cart123', '9876543210', 'Male', 23),
('JohnDoe', '1234', '9999911111', 'Male', 28),
('AliceW', 'abcd', '9999922222', 'Female', 24);

-- Seed categories
INSERT INTO categories (name, description) VALUES
('Smartphones', 'All types of smartphones'),
('Laptops', 'Various laptops'),
('Earbuds', 'Wireless & wired earbuds'),
('Books', 'Educational & programming books'),
('Dailywears', 'Clothing & daily apparel');

-- Seed products
INSERT INTO products (category_id, name, price_inr, stock_qty) VALUES
-- Smartphones
((SELECT id FROM categories WHERE name='Smartphones'), 'Smartphone XYZ', 25000.00, 50),
-- Laptops
((SELECT id FROM categories WHERE name='Laptops'), 'Laptop ABC', 55000.00, 30),
-- Earbuds
((SELECT id FROM categories WHERE name='Earbuds'), 'Earbuds Pro', 4500.00, 100),
-- Books
((SELECT id FROM categories WHERE name='Books'), 'Java Programming Guide', 800.00, 75),
-- Dailywears
((SELECT id FROM categories WHERE name='Dailywears'), 'CoolFit T-Shirt', 600.00, 200),
((SELECT id FROM categories WHERE name='Dailywears'), 'SlimFit Jeans', 1200.00, 120);

-- Create a sample ACTIVE cart for NihilashL with two items
INSERT INTO carts (user_id, status)
VALUES ((SELECT id FROM users WHERE username='NihilashL'), 'ACTIVE');

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price_inr)
VALUES
(
  (SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE username='NihilashL') AND status='ACTIVE' LIMIT 1),
  (SELECT id FROM products WHERE name='Smartphone XYZ' LIMIT 1),
  1,
  (SELECT price_inr FROM products WHERE name='Smartphone XYZ' LIMIT 1)
),
(
  (SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE username='NihilashL') AND status='ACTIVE' LIMIT 1),
  (SELECT id FROM products WHERE name='Earbuds Pro' LIMIT 1),
  2,
  (SELECT price_inr FROM products WHERE name='Earbuds Pro' LIMIT 1)
);
select*from cart_items;
select*from carts;
select*from products;
select*from categories;
select*from users;
