package com.library.dao;

import java.sql.*;

public class CategoryDao {

    // 检查分类是否存在
    public boolean categoryExists(Connection conn, int categoryId, String categoryName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM category WHERE categoryid = ? AND categoryname = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.setString(2, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // 创建新分类
    public boolean createCategory(Connection conn, int categoryId, String categoryName) throws SQLException {
        String sql = "INSERT INTO category (categoryid, categoryname) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.setString(2, categoryName);
            return stmt.executeUpdate() > 0;
        }
    }
}