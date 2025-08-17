package model;

public class Product {
    private int id;
    private String name;
    private int categoryId;
    private double price;  // note: not getPriceInr(), just price
    private int stockQty;

    public Product() {}

    public Product(int id, String name, int categoryId, double price, int stockQty) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQty = stockQty;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public double getPrice() { return price; }      // use this in DAO
    public int getStockQty() { return stockQty; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setPrice(double price) { this.price = price; }
    public void setStockQty(int stockQty) { this.stockQty = stockQty; }
}
