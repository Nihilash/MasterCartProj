package model;

public class Cart {
    private int id;
    private int userId;
    private String status;  // ACTIVE, CHECKED_OUT

    public Cart() {}

    public Cart(int id, int userId, String status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
