package com.library.model;

public class Reader implements User {
    private Integer readerId;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String contact;
    private Integer maxBorrow = 5;
    private String status = "A";

    // Getter & Setter 方法
    public Integer getReaderId() { return readerId; }
    public void setReaderId(Integer readerId) { this.readerId = readerId; }
    public Reader() {}
//    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    private String role = "user";

//    public String getRole() {
//        return role;
//    }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) {
        this.role = role.toUpperCase();
    }
//    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public Integer getUserId() {
        return this.readerId; // 返回 readerId
    }
    @Override
    public String getRole() {
        return this.role;
    }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Reader(int readerId, String username, String name, String status) {
        this.readerId = readerId;
        this.username = username;
        this.name = name;
        this.status = status;
    }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Integer getMaxBorrow() { return maxBorrow; }
    public void setMaxBorrow(Integer maxBorrow) { this.maxBorrow = maxBorrow; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}