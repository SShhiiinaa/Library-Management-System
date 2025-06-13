package com.library.model;

import java.util.List;

public class Admin implements User {
    private int adminId;
    private String username;
    private String name;
    private String password;
    private List<String> roles; // 角色名列表，RBAC支持

    public Admin() {}

    public Admin(int adminId, String username, String name, String password) {
        this.adminId = adminId;
        this.username = username;
        this.name = name;
        this.password = password;
    }

    @Override
    public Integer getUserId() {
        return adminId;
    }
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserType() {
        return "A";
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }
    @Override
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}