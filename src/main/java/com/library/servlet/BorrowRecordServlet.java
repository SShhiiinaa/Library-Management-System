package com.library.servlet;

import com.library.dao.BorrowRecordDao;
import com.library.model.BorrowRecord;
import com.library.model.User;
import com.library.util.DatabaseUtil;
import com.library.util.OperationLogger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/borrowRecord")
public class BorrowRecordServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 1. 检查用户是否登录
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. 获取借阅记录
        try (Connection conn = DatabaseUtil.getConnection()) {
            BorrowRecordDao dao = new BorrowRecordDao();
            List<BorrowRecord> records = dao.getRecordsByReader(conn, user.getUserId());

            // 3. 调试日志（可选）
            System.out.println("[DEBUG] 用户ID: " + user.getUserId() + ", 记录数: " + records.size());

            // 4. 传递数据到 JSP
            request.setAttribute("records", records);

            // 5. 操作日志
            OperationLogger.log(request, "view_borrow_record", user.getUserId()+"", "success", "用户查看借阅记录，记录数：" + records.size());

        } catch (SQLException e) {
            // 6. 异常处理
            e.printStackTrace();
            request.setAttribute("error", "获取借阅记录失败: " + e.getMessage());
            OperationLogger.log(request, "view_borrow_record", user.getUserId()+"", "fail", "获取借阅记录失败：" + e.getMessage());
        }

        // 7. 转发到 JSP
        request.getRequestDispatcher("/borrowRecord.jsp").forward(request, response);
    }
}