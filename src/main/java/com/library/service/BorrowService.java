package com.library.service;

import com.library.dao.BookDao;
import com.library.dao.BorrowDao;
import com.library.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;

public class BorrowService {

    // 修改参数类型为String
    public boolean borrowBook(int userId, String isbn) {
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);

            BookDao bookDao = new BookDao();
            // 使用ISBN查询库存
            int stock = bookDao.getStock(conn, isbn);
            if (stock < 1) return false;

            // 正确调用方式（三个参数）
            bookDao.updateStock(conn, isbn, -1);


            // 创建借阅记录（需要同步修改BorrowDao）
            BorrowDao borrowDao = new BorrowDao();
            borrowDao.createRecord(conn, userId, isbn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); }
                catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            DatabaseUtil.closeConnection(conn);
        }
    }
}