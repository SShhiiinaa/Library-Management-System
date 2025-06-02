package com.library.servlet;

import com.library.dao.BorrowRecordDao;
import com.library.dao.BookDao;
import com.library.model.Reader;
import com.library.model.BorrowRecord;
import com.library.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.library.model.User;


@WebServlet("/returnBooks")
public class ReturnBooksServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 展示未归还书籍
        showUnreturnedBooks(request, response, null, null);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 处理还书
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String recordIdStr = request.getParameter("recordId");
        String error = null, success = null;
        if (recordIdStr == null) {
            error = "参数错误";
            showUnreturnedBooks(request, response, error, null);
            return;
        }
        int recordId = Integer.parseInt(recordIdStr);
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);

            BorrowRecordDao dao = new BorrowRecordDao();
            String isbn = dao.returnBookByRecordId(conn, recordId, user.getUserId());
            if (isbn == null) {
                error = "归还失败：未找到借阅记录";
                throw new SQLException(error);
            }
            BookDao bookDao = new BookDao();
            bookDao.updateStock(conn, isbn, 1);

            conn.commit();
            success = "归还成功！";
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            error = e.getMessage();
        } finally {
            DatabaseUtil.closeConnection(conn);
        }
        showUnreturnedBooks(request, response, error, success);
    }

    private void showUnreturnedBooks(HttpServletRequest request, HttpServletResponse response, String error, String success)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try (Connection conn = DatabaseUtil.getConnection()) {
            BorrowRecordDao dao = new BorrowRecordDao();
            List<BorrowRecord> records = dao.getUnreturnedRecordsByReader(conn, user.getUserId());
            request.setAttribute("records", records);
        } catch (SQLException e) {
            request.setAttribute("error", "数据库错误: " + e.getMessage());
        }
        if (error != null) request.setAttribute("error", error);
        if (success != null) request.setAttribute("success", success);
        request.getRequestDispatcher("/returnBooks.jsp").forward(request, response);
    }
}