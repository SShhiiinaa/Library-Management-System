package com.library.dao;

import com.library.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDao {
    // 为用户分配角色
    public void addUserRole(int userId, int roleId, String userType) throws SQLException {
        String sql = "INSERT INTO user_roles (userid, roleid, usertype) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, roleId);
            pstmt.setString(3, userType);
            pstmt.executeUpdate();
        }
    }

    // 移除用户角色
    public void removeUserRole(int userId, int roleId, String userType) throws SQLException {
        String sql = "DELETE FROM user_roles WHERE userid=? AND roleid=? AND usertype=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, roleId);
            pstmt.setString(3, userType);
            pstmt.executeUpdate();
        }
    }

    // 查询某用户所有角色名
    public List<String> getRoleNamesByUserId(int userId, String userType) throws SQLException {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT r.rolename FROM user_roles ur JOIN roles r ON ur.roleid = r.roleid WHERE ur.userid = ? AND ur.usertype = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, userType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("rolename"));
                }
            }
        }
        return roles;
    }

    // 查询某用户所有角色ID
    public List<Integer> getRoleIdsByUserId(int userId, String userType) throws SQLException {
        List<Integer> roleIds = new ArrayList<>();
        String sql = "SELECT roleid FROM user_roles WHERE userid = ? AND usertype = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, userType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    roleIds.add(rs.getInt("roleid"));
                }
            }
        }
        return roleIds;
    }
}