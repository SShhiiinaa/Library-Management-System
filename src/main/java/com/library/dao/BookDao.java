package com.library.dao;

import com.library.model.Book;
import com.library.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public int getStock(Connection conn, String isbn) throws SQLException {
        String sql = "SELECT stockqty FROM books WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("stockqty") : 0;
            }
        }
    }

    // 保持原有参数结构
    public void updateStock(Connection conn, String isbn, int delta) throws SQLException {
        String sql = "UPDATE books SET stockqty = stockqty + ? WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

    public List<Book> searchBooks(Connection conn, String title, String author, String isbn)
            throws SQLException {

        List<Book> books = new ArrayList<>();
        StringBuilder sql = new StringBuilder()
                .append("SELECT b.isbn, b.title, b.author, b.categoryid, c.categoryname, b.retailprice, b.stockqty ")
                .append("FROM books b ")
                .append("LEFT JOIN category c ON b.categoryid = c.categoryid ")
                .append("WHERE 1=1");

        List<String> params = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            sql.append(" AND LOWER(b.title) LIKE ?");
            params.add("%" + title.toLowerCase() + "%");
        }
        if (author != null && !author.isEmpty()) {
            sql.append(" AND LOWER(b.author) LIKE ?");
            params.add("%" + author.toLowerCase() + "%");
        }
        if (isbn != null && !isbn.isEmpty()) {
            sql.append(" AND b.isbn = ?");
            params.add(isbn);
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setIsbn(rs.getString("isbn"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setCategoryId(rs.getInt("categoryid"));
                    book.setCategoryName(rs.getString("categoryname"));
                    book.setRetailPrice(rs.getBigDecimal("retailprice"));
                    book.setStockQty(rs.getInt("stockqty"));
                    books.add(book);
                }
            }
        }
        return books;
    }
    public List<Book> getAllBooks(Connection conn) throws SQLException {
        String sql = "SELECT b.*, c.categoryname " +
                "FROM books b " +
                "LEFT JOIN category c ON b.categoryid = c.categoryid";

        List<Book> books = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = extractBookFromResultSet(rs);
                book.setCategoryName(rs.getString("categoryname"));
                book.setCategoryId(rs.getInt("categoryid"));
                books.add(book);
            }
        }
        return books;
    }

    // 添加新书
    public boolean addBook(Connection conn, Book book) throws SQLException {
        String sql = "INSERT INTO books (isbn, title, author, categoryid, retailprice, stockqty) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getCategoryId());
            stmt.setBigDecimal(5, book.getRetailPrice());
            stmt.setInt(6, book.getStockQty());

            return stmt.executeUpdate() > 0;
        }
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setRetailPrice(rs.getBigDecimal("retailprice"));
        book.setStockQty(rs.getInt("stockqty"));
        return book;
    }

    // 修改图书信息
    public boolean updateBook(Connection conn, Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, categoryid=?, retailprice=?, stockqty=? WHERE isbn=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getCategoryId());
            stmt.setBigDecimal(4, book.getRetailPrice());
            stmt.setInt(5, book.getStockQty());
            stmt.setString(6, book.getIsbn());
            return stmt.executeUpdate() > 0;
        }
    }

    // 删除图书
    public boolean deleteBook(Connection conn, String isbn) throws SQLException {
        String sql = "DELETE FROM books WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;
        }
    }
}