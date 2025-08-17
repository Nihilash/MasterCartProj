USE mastercart;

-- Seed users (passwords are plain for seed; later store bcrypt hashes)
INSERT INTO users (username, password_hash, mobile, gender, age)
VALUES
('Nihilash', 'Cart123', '9876543210', 'Male', 23),
('john', '1234', '9999911111', 'Male', 28),
('alice', 'abcd', '9999922222', 'Female', 24);

-- Seed categories
INSERT INTO categories (name, description) VALUES
('Dairy', 'Milk & dairy products'),
('Electronics', 'Consumer electronics'),
('Cosmetics', 'Beauty & personal care'),
('Stationaries', 'Office & school supplies'),
('Dailywears', 'Clothing & daily apparel');

-- Seed products
INSERT INTO products (category_id, name, price_inr, stock_qty) VALUES
-- Dairy
((SELECT id FROM categories WHERE name='Dairy'), 'Milk', 50.00, 100),
((SELECT id FROM categories WHERE name='Dairy'), 'Cheese', 80.00, 50),
((SELECT id FROM categories WHERE name='Dairy'), 'Curd', 40.00, 70),

-- Electronics
((SELECT id FROM categories WHERE name='Electronics'), 'Mobile', 15000.00, 20),
((SELECT id FROM categories WHERE name='Electronics'), 'Laptop', 50000.00, 10),
((SELECT id FROM categories WHERE name='Electronics'), 'Headphones', 2000.00, 35),

-- Cosmetics
((SELECT id FROM categories WHERE name='Cosmetics'), 'Lipstick', 300.00, 60),
((SELECT id FROM categories WHERE name='Cosmetics'), 'Perfume', 800.00, 25),
((SELECT id FROM categories WHERE name='Cosmetics'), 'Moisturizer', 250.00, 40),

-- Stationaries
((SELECT id FROM categories WHERE name='Stationaries'), 'Notebook', 60.00, 200),
((SELECT id FROM categories WHERE name='Stationaries'), 'Pen', 10.00, 500),
((SELECT id FROM categories WHERE name='Stationaries'), 'File', 25.00, 150),

-- Dailywears
((SELECT id FROM categories WHERE name='Dailywears'), 'T-Shirt', 500.00, 80),
((SELECT id FROM categories WHERE name='Dailywears'), 'Jeans', 1000.00, 60),
((SELECT id FROM categories WHERE name='Dailywears'), 'Jacket', 1500.00, 30);

-- Create a sample ACTIVE cart for Nihilash with two items
INSERT INTO carts (user_id, status)
VALUES ((SELECT id FROM users WHERE username='Nihilash'), 'ACTIVE');

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price_inr)
VALUES
(
  (SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE username='Nihilash') AND status='ACTIVE' LIMIT 1),
  (SELECT id FROM products WHERE name='Milk' LIMIT 1),
  2,
  (SELECT price_inr FROM products WHERE name='Milk' LIMIT 1)
),
(
  (SELECT id FROM carts WHERE user_id = (SELECT id FROM users WHERE username='Nihilash') AND status='ACTIVE' LIMIT 1),
  (SELECT id FROM products WHERE name='Headphones' LIMIT 1),
  1,
  (SELECT price_inr FROM products WHERE name='Headphones' LIMIT 1)
);
