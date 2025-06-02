package com.library.model;

public class Admin implements User{
    private int adminId;
    private String username;
    private String name;
    private String role = "ADMIN";

//    public String getRole() {
//        return role;
//    }
    // 添加带参构造函数
    public Admin(int adminId, String username) {
        this.adminId = adminId;
        this.username = username;}
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    // 添加缺失的setter方法
    public void setName(String name) {
        this.name = name;
    }
    public Integer getUserId() { return adminId; }

    public void setRole(String role) {
        this.role = role.toUpperCase();
    }
}
// 构造函数、getters、setters