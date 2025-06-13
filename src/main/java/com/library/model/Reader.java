package com.library.model;

import java.util.List;

public class Reader implements User {
    private Integer readerId;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String contact;
    private Integer maxBorrow = 5;
    private String status = "A";
    private List<String> roles; // 多角色支持

    public Reader() {}

    public Reader(int readerId, String username, String name, String status) {
        this.readerId = readerId;
        this.username = username;
        this.name = name;
        this.status = status;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    @Override
    public Integer getUserId() {
        return readerId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getMaxBorrow() {
        return maxBorrow;
    }

    public void setMaxBorrow(Integer maxBorrow) {
        this.maxBorrow = maxBorrow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getUserType() {
        return "R";
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