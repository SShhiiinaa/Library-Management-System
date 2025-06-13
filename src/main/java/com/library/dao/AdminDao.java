package com.library.dao;

import com.library.model.Admin;
import com.library.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    // 创建管理员
    public void createAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO administrators (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPassword());
            pstmt.executeUpdate();
        }
    }

    // 查询所有管理员
    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM administrators ORDER BY adminid";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Admin a = new Admin();
                a.setAdminId(rs.getInt("adminid"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                list.add(a);
            }
        }
        return list;
    }

    // 通过ID查找管理员
    public Admin getAdminById(int adminId) throws SQLException {
        String sql = "SELECT * FROM administrators WHERE adminid=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin a = new Admin();
                    a.setAdminId(rs.getInt("adminid"));
                    a.setUsername(rs.getString("username"));
                    a.setPassword(rs.getString("password"));
                    return a;
                }
            }
        }
        return null;
    }

    // 通过用户名查找管理员
    public Admin getAdminByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM administrators WHERE username=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin a = new Admin();
                    a.setAdminId(rs.getInt("adminid"));
                    a.setUsername(rs.getString("username"));
                    a.setPassword(rs.getString("password"));
                    return a;
                }
            }
        }
        return null;
    }

    // 修改管理员密码
    public boolean updateAdminPassword(int adminId, String newPassword) throws SQLException {
        String sql = "UPDATE administrators SET password=? WHERE adminid=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, adminId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // 检查用户名唯一性
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM administrators WHERE username=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}