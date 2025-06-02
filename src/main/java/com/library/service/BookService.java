package com.library.service;

import com.library.model.Book;
import com.library.model.Category;
import com.library.util.DatabaseUtil;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    // 核心搜索方法（严格匹配数据库设计）
    public List<Book> searchBooks(String title, String author, String isbn, Integer categoryId)
            throws SQLException {

        StringBuilder sql = new StringBuilder()
                .append("SELECT ")
                .append("  b.ISBN, b.TITLE, b.AUTHOR, ")
                .append("  b.CATEGORYID, b.PUBLISHERID, ")
                .append("  b.PUBLISHDATE, b.RETAILPRICE, b.STOCKQTY, ")
                .append("  NVL(c.CATEGORYNAME, '未分类') AS CATEGORY_NAME, ")  // Oracle空值处理
                .append("  NVL(p.NAME, '未知出版社') AS PUBLISHER_NAME ")       // 出版社空值处理
                .append("FROM BOOKS b ")
                .append("LEFT JOIN CATEGORY c ON b.CATEGORYID = c.CATEGORYID ")  // 修正关联字段
                .append("LEFT JOIN PUBLISHERS p ON b.PUBLISHERID = p.PUBLISHERID ") // 关键修正点
                .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // 查询条件处理（符合字段约束）
        if (title != null && !title.trim().isEmpty()) {
            sql.append("AND UPPER(b.TITLE) LIKE ? ");
            params.add("%" + title.trim().toUpperCase() + "%");
        }
        if (author != null && !author.trim().isEmpty()) {
            sql.append("AND UPPER(b.AUTHOR) LIKE ? ");
            params.add("%" + author.trim().toUpperCase() + "%");
        }
        if (isbn != null && !isbn.trim().isEmpty()) {
            sql.append("AND b.ISBN = ? ");
            params.add(isbn.trim());
        }
        if (categoryId != null) {
            sql.append("AND b.CATEGORYID = ? ");
            params.add(categoryId);
        }

        sql.append("ORDER BY b.PUBLISHDATE DESC");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            // 参数绑定（防SQL注入）
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            return processResultSet(pstmt.executeQuery());
        }
    }

    // 结果集映射（严格类型检查）
    private List<Book> processResultSet(ResultSet rs) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = new Book();
            // 基础字段
            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("TITLE"));
            book.setAuthor(rs.getString("AUTHOR"));
            Integer categoryId = rs.getObject("CATEGORYID", Integer.class);
            book.setCategoryId(categoryId != null ? categoryId : 0); // 允许 0 或业务默认值

            Integer publisherId = rs.getObject("PUBLISHERID", Integer.class);
            book.setPublisherId(publisherId != null ? publisherId : 0);

            // 日期处理（java.sql.Date -> java.util.Date）
            Date sqlDate = rs.getDate("PUBLISHDATE");
            book.setPublishDate(sqlDate != null ? new java.util.Date(sqlDate.getTime()) : null);


            BigDecimal price = rs.getObject("RETAILPRICE", BigDecimal.class);
            book.setRetailPrice(price != null ? price : BigDecimal.ZERO);


            // 库存处理（空值默认 0）
            Integer stockQty = rs.getObject("STOCKQTY", Integer.class);
            book.setStockQty(stockQty != null ? stockQty : 0);

            // 关联字段
            book.setCategoryName(rs.getString("CATEGORY_NAME"));
            book.setPublisherName(rs.getString("PUBLISHER_NAME"));

            books.add(book);
        }
        return books;
    }

    // 获取全部分类（支持树形结构）
    public List<Category> getAllCategories() throws SQLException {
        final String SQL = "SELECT CATEGORYID, CATEGORYNAME FROM CATEGORY";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("CATEGORYID"));
                category.setCategoryName(rs.getString("CATEGORYNAME"));
                // 处理可空PARENTID
                categories.add(category);
            }
        }
        return categories;
    }

}