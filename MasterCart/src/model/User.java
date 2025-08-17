package model;

public class User {
    private int id;
    private String username;   // maps to DB: users.username
    private String mobile;     // maps to DB: users.mobile
    private String passwordHash; // maps to DB: users.password_hash

    public User() {}

    public User(int id, String username, String mobile, String passwordHash) {
        this.id = id;
        this.username = username;
        this.mobile = mobile;
        this.passwordHash = passwordHash;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
