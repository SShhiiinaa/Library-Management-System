package com.library.servlet;

import com.library.dao.BookDao;
import com.library.dao.BorrowRecordDao;
import com.library.dao.ReaderDAO;
import com.library.model.Book;
import com.library.model.Reader;
import com.library.util.DatabaseUtil;
import com.library.util.InputSanitizer;
import com.library.util.OperationLogger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.library.model.User;

@WebServlet("/borrow")
public class BorrowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            BookDao bookDao = new BookDao();

            // 获取搜索参数并防注入
            String title = InputSanitizer.sanitizeText(request.getParameter("title"), 50);
            String author = InputSanitizer.sanitizeText(request.getParameter("author"), 30);
            String isbn = InputSanitizer.sanitizeIdentifier(request.getParameter("isbn"), 20);

            List<Book> books = bookDao.searchBooks(conn, title, author, isbn);
            request.setAttribute("books", books);

        } catch (SQLException e) {
            request.setAttribute("error", "数据库错误: " + InputSanitizer.sanitizeForHtml(e.getMessage()));
        }

        request.getRequestDispatcher("/borrow.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String isbn = InputSanitizer.sanitizeIdentifier(request.getParameter("isbn"), 20);
        String error = null;
        String success = null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);

            BookDao bookDao = new BookDao();
            BorrowRecordDao borrowDao = new BorrowRecordDao();
            ReaderDAO readerDao = new ReaderDAO();

            // 1. 查询当前用户Reader状态
            Reader reader = readerDao.getReaderById(user.getUserId());
            if (reader == null) {
                error = "账户不存在";
                OperationLogger.log(request, "borrow_book", isbn, "fail", error);
                throw new SQLException(error);
            }

            // 2. 如果账户已禁用，不允许借书
            if (!"A".equals(reader.getStatus())) {
                error = "账户已禁用，无法借书. 寻求管理员解封";
                OperationLogger.log(request, "borrow_book", isbn, "fail", error);
                throw new SQLException(error);
            }

            // 3. 检查逾期记录，逾期则禁用账户，提示并禁止借书
            if (borrowDao.hasOverdueBooks(conn, user.getUserId())) {
                reader.setStatus("I"); // 禁用账户
                readerDao.updateReaderWithoutPassword(reader); // 只改状态不改密码
                error = "存在逾期未还书籍，账户已被禁用，仅可还书";
                OperationLogger.log(request, "borrow_book", isbn, "fail", error);
                throw new SQLException(error);
            }

            // 4. 检查库存
            int currentStock = bookDao.getStock(conn, isbn);
            if (currentStock < 1) {
                error = "该书籍库存不足";
                OperationLogger.log(request, "borrow_book", isbn, "fail", error);
                throw new SQLException(error);
            }

            // 5. 执行借阅操作
            borrowDao.createBorrowRecord(conn, user.getUserId(), isbn);
            bookDao.updateStock(conn, isbn, -1);

            conn.commit();
            success = "借阅成功！请于30日内归还";
            OperationLogger.log(request, "borrow_book", isbn, "success", "借阅成功");

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            error = e.getMessage() != null ? InputSanitizer.sanitizeForHtml(e.getMessage()) : "借阅操作失败";
        } finally {
            DatabaseUtil.closeConnection(conn);
        }

        request.setAttribute("error", error);
        request.setAttribute("success", success);
        doGet(request, response);
    }
}