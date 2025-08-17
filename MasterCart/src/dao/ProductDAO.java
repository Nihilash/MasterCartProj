package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDAO {

    // Insert new product
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, category_id, price_inr, stock_qty) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setInt(2, product.getCategoryId());  // matches Product field
            ps.setDouble(3, product.getPrice());    // changed from getPriceInr()
            ps.setInt(4, product.getStockQty());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("category_id"),
                    rs.getDouble("price_inr"),  // maps to price in Product constructor
                    rs.getInt("stock_qty")
                );
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
