package model;

public class CartItem {
    private int id;
    private Product product;   // the Product object
    private int quantity;
    private double unitPriceInr; // make sure the field name is exactly this

    public CartItem(int id, Product product, int quantity, double unitPriceInr) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.unitPriceInr = unitPriceInr;
    }

    // Getters
    public int getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    // ✅ This must exist
    public double getUnitPriceInr() { return unitPriceInr; }

    // Utility: line total
    public double getLineTotal() {
        return quantity * unitPriceInr;
    }

    // Setters (optional)
    public void setId(int id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPriceInr(double unitPriceInr) { this.unitPriceInr = unitPriceInr; }
}
