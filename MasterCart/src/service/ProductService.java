package service;

import dao.ProductDAO;
import model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // Add a product
    public void addProduct(Product product) {
        productDAO.addProduct(product);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    // Get product by ID
    public Product getProductById(int id) {
        for (Product p : productDAO.getAllProducts()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    //  Get product by category name
    public Product getProductByCategory(String categoryName) {
        int categoryId = getCategoryIdFromName(categoryName);
        if (categoryId == -1) return null; // unknown category

        for (Product p : productDAO.getAllProducts()) {
            if (p.getCategoryId() == categoryId) {
                return p;
            }
        }
        return null;
    }

    // Helper: map category name to categoryId
    private int getCategoryIdFromName(String name) {
        switch (name.toLowerCase()) {
            case "smartphone": return 1;
            case "laptop": return 2;
            case "earbuds": return 3;
            case "books": return 4;
            case "t-shirt": return 5;
            case "jeans": return 6;
            default: return -1; // unknown
        }
    }
}
