package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BorrowDao {
    // 修改参数类型为String
    public void createRecord(Connection conn, int userId, String isbn)
            throws SQLException {

        String sql = "INSERT INTO borrow_records (readerid, isbn) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }
}