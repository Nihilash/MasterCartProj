# 🛒 MasterCart – Java Swing E-Commerce Cart System

Hello there! 👋  
I’m **Nihilash L V**, a passionate Java Developer (Fresher – Software Development & Testing Track).  
**MasterCart** is my end-to-end desktop-based mini e-commerce cart system built using **Java, Swing, JDBC, and MySQL**.

Think of MasterCart as a desktop shopping cart where you can:

✔️ Register/Login as a user  
✔️ Browse products  
✔️ Add/Remove items from cart  
✔️ Adjust quantities  
✔️ Checkout and **generate a styled HTML invoice with background image**  
✔️ **Open invoice in your preferred browser** (Chrome, Edge, Brave, Firefox, Safari, etc.)  

This project reflects my journey in **Java development & testing**, combining both development (Swing + DB) and a testing mindset (validation, reliability). 🚀

---

## 📂 Project Structure
```plaintext
MasterCartApp/
│
├── src/
│ ├── main/ # Application entry point
│ │ └── MasterCartApp.java
│ │
│ ├── dao/ # Data Access Layer
│ │ ├── ProductDAO.java
│ │ ├── UserDAO.java
│ │ └── DBConnection.java
│ │
│ ├── model/ # Data Models
│ │ ├── User.java
│ │ ├── Product.java
│ │ ├── Cart.java
│ │ ├── CartItem.java
│ │ ├── Order.java
│ │
│ ├── service/ # Business Logic
│ │ ├── ProductService.java
│ │ ├── UserService.java
│ │ ├── CartService.java
│ │
│ ├── ui/ # Swing GUI Screens
│ │ ├── LoginUI.java
│ │ ├── RegisterUI.java
│ │ ├── ProductUI.java
│ │ ├── CartUI.java
│ │ └── PDFGenerator.java # Styled HTML Invoice Generator
│ │
│ └── utils/
│ └── Helpers.java
│
├── MasterCartimages/ # UI & Invoice background images
│ └── invoice.jpg
│
├── database/
│ └── schema.sql # MySQL tables
│
├── invoices/ # Generated invoices (HTML)
│
└── README.md
```
pgsql
---

## 🛠️ Tech Stack

### 🔹 Development
- **Java 8+** → Core OOP, Collections, Exception Handling  
- **Swing (AWT)** → GUI for Login, Product Catalog, Cart, Invoice Generator  
- **JDBC** → Database connectivity  

### 🔹 Database
- **MySQL 8.0** → Relational database  
- **Schema Design** → Normalized structure (Users, Products, Cart, Orders)  
- **CRUD Operations** → Insert, Select, Update, Delete  

### 🔹 Utilities & Tools
- **Git & GitHub** → Version control  
- **Eclipse / IntelliJ IDEA** → IDE  
- **MySQL Workbench** → Database design & query testing  

---

## 🧾 Database Tables

### 👤 Users Table
| Column Name | Description |
|-------------|-------------|
| user_id     | Unique ID for each user |
| username    | Login username |
| password    | Encrypted password |
| email       | Unique email for identity |

### 📦 Products Table
| Column Name | Description |
|-------------|-------------|
| product_id  | Unique ID for each product |
| name        | Product name |
| price       | Cost of the product |
| stock       | Available quantity |

### 🛒 Cart Table
| Column Name | Description |
|-------------|-------------|
| cart_id     | Unique cart identifier |
| user_id     | Reference to the user |
| product_id  | Reference to product |
| quantity    | Quantity added by user |

### 📑 Orders Table
| Column Name | Description |
|-------------|-------------|
| order_id    | Unique order identifier |
| user_id     | Customer who placed the order |
| date        | Date of order |
| total       | Final billed amount |

---

## ✨ Features Implemented So Far
-  **User registration & login** with validation  
-  **Browse & search products**  
-  **Add/remove/update cart items**  
-  **Styled HTML invoice generation** with background image  
-  **Transparent invoice popup style with cursive fonts**  
-  **Option to open invoice in browser** (System Default, Chrome, Edge, Brave, Firefox, Safari, Explorer)  

---

## 🚀 Future Enhancements
- 🔹 User authentication with password hashing (BCrypt/Argon2)  
- 🔹 Product categories & search filters  
- 🔹 REST API layer for web/mobile extension  
- 🔹 PDF invoice generation (instead of only HTML)  
- 🔹 Role-based access (Admin Panel for product management)  

---

## 👤 Author
**Nihilash L V**  
💻 Java Developer (Fresher – Development & Testing Track)  
📩 Reach me via GitHub  

---
