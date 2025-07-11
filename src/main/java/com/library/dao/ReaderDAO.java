package com.library.dao;

import com.library.model.Reader;
import com.library.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {
    // 创建读者
    public void createReader(Reader reader) throws SQLException {
        String sql = "INSERT INTO readers (username, password, name, gender, contact, maxborrow, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reader.getUsername());
            pstmt.setString(2, reader.getPassword());
            pstmt.setString(3, reader.getName());
            pstmt.setString(4, reader.getGender());
            pstmt.setString(5, reader.getContact());
            pstmt.setInt(6, reader.getMaxBorrow());
            pstmt.setString(7, reader.getStatus());
            pstmt.executeUpdate();
        }
    }

    // 查询所有读者
    public List<Reader> getAllReaders() throws SQLException {
        List<Reader> list = new ArrayList<>();
        String sql = "SELECT * FROM readers ORDER BY readerid";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Reader r = extractReader(rs);
                list.add(r);
            }
        }
        return list;
    }

    // 通过ID查找读者
    public Reader getReaderById(int readerId) throws SQLException {
        String sql = "SELECT * FROM readers WHERE readerid=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, readerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractReader(rs);
                }
            }
        }
        return null;
    }

    // 根据用户名查找读者
    public Reader getReaderByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM readers WHERE username=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractReader(rs);
                }
            }
        }
        return null;
    }

    // 搜索读者（用户名或姓名模糊）
    public List<Reader> findReadersByKeyword(String keyword) throws SQLException {
        List<Reader> list = new ArrayList<>();
        String sql = "SELECT * FROM readers WHERE username LIKE ? OR name LIKE ? ORDER BY readerid";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String likeKey = "%" + keyword + "%";
            pstmt.setString(1, likeKey);
            pstmt.setString(2, likeKey);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reader r = extractReader(rs);
                    list.add(r);
                }
            }
        }
        return list;
    }

    // 修改读者
    public void updateReader(Reader reader) throws SQLException {
        String sql = "UPDATE readers SET username=?, password=?, name=?, gender=?, contact=?, maxborrow=?, status=? WHERE readerid=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reader.getUsername());
            pstmt.setString(2, reader.getPassword());
            pstmt.setString(3, reader.getName());
            pstmt.setString(4, reader.getGender());
            pstmt.setString(5, reader.getContact());
            pstmt.setInt(6, reader.getMaxBorrow());
            pstmt.setString(7, reader.getStatus());
            pstmt.setInt(8, reader.getUserId());
            pstmt.executeUpdate();
        }
    }

    // 修改读者（不改密码）
    public void updateReaderWithoutPassword(Reader reader) throws SQLException {
        String sql = "UPDATE readers SET username=?, name=?, gender=?, contact=?, maxborrow=?, status=? WHERE readerid=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reader.getUsername());
            pstmt.setString(2, reader.getName());
            pstmt.setString(3, reader.getGender());
            pstmt.setString(4, reader.getContact());
            pstmt.setInt(5, reader.getMaxBorrow());
            pstmt.setString(6, reader.getStatus());
            pstmt.setInt(7, reader.getUserId());
            pstmt.executeUpdate();
        }
    }

    // 软删除读者（将状态更改为'D'）
    public boolean softDeleteReader(int readerId) throws SQLException {
        String sql = "UPDATE readers SET status = 'D' WHERE readerid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            return stmt.executeUpdate() > 0;
        }
    }

    // 禁用读者（状态'I'）
    public boolean disableReader(int readerId) throws SQLException {
        String sql = "UPDATE readers SET status = 'I' WHERE readerid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            return stmt.executeUpdate() > 0;
        }
    }

    // 恢复读者（状态'A'）
    public boolean restoreReader(int readerId) throws SQLException {
        String sql = "UPDATE readers SET status = 'A' WHERE readerid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            return stmt.executeUpdate() > 0;
        }
    }

    // 检查用户名唯一性
    public boolean isUsernameExists(String username) throws SQLException {
        return checkFieldExists("username", username);
    }

    // 检查联系方式唯一性
    public boolean isContactExists(String contact) throws SQLException {
        return checkFieldExists("contact", contact);
    }

    // 通用字段存在性检查
    private boolean checkFieldExists(String field, String value) throws SQLException {
        String sql = "SELECT 1 FROM readers WHERE " + field + " = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // 抽取Reader对象
    private Reader extractReader(ResultSet rs) throws SQLException {
        Reader r = new Reader();
        r.setReaderId(rs.getInt("readerid"));
        r.setUsername(rs.getString("username"));
        r.setPassword(rs.getString("password"));
        r.setName(rs.getString("name"));
        r.setGender(rs.getString("gender"));
        r.setContact(rs.getString("contact"));
        r.setMaxBorrow(rs.getInt("maxborrow"));
        r.setStatus(rs.getString("status"));
        // roles字段由Service层填充
        return r;
    }
}