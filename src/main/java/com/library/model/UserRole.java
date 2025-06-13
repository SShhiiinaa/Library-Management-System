package com.library.model;

public class UserRole {
    private int userId;
    private int roleId;
    private String userType; // 'A' 管理员, 'R' 读者

    public UserRole() {}

    public UserRole(int userId, int roleId, String userType) {
        this.userId = userId;
        this.roleId = roleId;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}