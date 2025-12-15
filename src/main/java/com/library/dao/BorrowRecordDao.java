package com.library.dao;

import java.sql.*;
import com.library.model.BorrowRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class BorrowRecordDao {

    public void createBorrowRecord(Connection conn, int readerId, String isbn)
            throws SQLException {

        String sql = "INSERT INTO borrow_records (readerid, isbn) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

    public int countActiveBorrows(Connection conn, int readerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM borrow_records WHERE readerid = ? AND returndate IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public boolean hasOverdueBooks(Connection conn, int readerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM borrow_records " +
                "WHERE readerid = ? " +
                "AND duedate < SYSDATE " +
                "AND returndate IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<BorrowRecord> getRecordsByReader(Connection conn, int readerId) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, b.title, b.author " +
                "FROM borrow_records br " +
                "JOIN books b ON br.isbn = b.isbn " +
                "WHERE br.readerid = ? " +
                "ORDER BY br.borrowdate DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowRecord record = new BorrowRecord();
                    record.setRecordId(rs.getInt("recordid"));
                    record.setIsbn(rs.getString("isbn"));
                    record.setBorrowDate(rs.getDate("borrowdate"));
                    record.setDueDate(rs.getDate("duedate"));
                    record.setReturnDate(rs.getDate("returndate"));
                    record.setOverdueDays(rs.getInt("overduedays"));
                    record.setBookTitle(rs.getString("title")); // 扩展字段
                    record.setBookAuthor(rs.getString("author")); // 扩展字段
                    records.add(record);
                }
            }
        }
        return records;
    }
    // 查询用户所有未归还记录
    public List<BorrowRecord> getUnreturnedRecordsByReader(Connection conn, int readerId) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, b.title, b.author " +
                "FROM borrow_records br " +
                "JOIN books b ON br.isbn = b.isbn " +
                "WHERE br.readerid = ? AND br.returndate IS NULL " +
                "ORDER BY br.borrowdate DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowRecord record = new BorrowRecord();
                    record.setRecordId(rs.getInt("recordid"));
                    record.setIsbn(rs.getString("isbn"));
                    record.setBorrowDate(rs.getDate("borrowdate"));
                    record.setDueDate(rs.getDate("duedate"));
                    record.setBookTitle(rs.getString("title"));
                    record.setBookAuthor(rs.getString("author"));
                    records.add(record);
                }
            }
        }
        return records;
    }

    // 通过记录ID归还图书，返回isbn
    public String returnBookByRecordId(Connection conn, int recordId, int readerId) throws SQLException {
        String selectSql = "SELECT isbn FROM borrow_records WHERE recordid = ? AND readerid = ? AND returndate IS NULL";
        String isbn = null;
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setInt(1, recordId);
            ps.setInt(2, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isbn = rs.getString("isbn");
                }
            }
        }
        if (isbn != null) {
            String updateSql = "UPDATE borrow_records SET returndate = SYSDATE, overduedays = GREATEST(0, TRUNC(SYSDATE) - TRUNC(duedate)) " +
                    "WHERE recordid = ? AND readerid = ? AND returndate IS NULL";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, recordId);
                ps.setInt(2, readerId);
                ps.executeUpdate();
            }
        }
        return isbn;
    }
}
