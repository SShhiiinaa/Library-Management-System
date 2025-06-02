package com.library.servlet;

import com.library.dao.ReaderDAO;
import com.library.model.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/readerManage")
public class ReaderManageServlet extends HttpServlet {
    private ReaderDAO dao = new ReaderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        String search = request.getParameter("search");

        try {
            if ("edit".equals(action) && idStr != null) {
                Reader reader = dao.getReaderById(Integer.parseInt(idStr));
                request.setAttribute("reader", reader);
                request.getRequestDispatcher("readerEdit.jsp").forward(request, response);
                return;
            } else if ("delete".equals(action)) {
                int readerId = Integer.parseInt(request.getParameter("id"));
                if (dao.softDeleteReader(readerId)) {
                    request.setAttribute("success", "读者已标记为删除");
                } else {
                    request.setAttribute("error", "删除读者失败");
                }
            } else if ("disable".equals(action)) {
                // 新增：处理禁用功能
                int readerId = Integer.parseInt(request.getParameter("id"));
                if (dao.disableReader(readerId)) {
                    request.setAttribute("success", "读者账户已被禁用");
                } else {
                    request.setAttribute("error", "禁用读者失败");
                }
            } else if ("restore".equals(action)) {
                // 新增：处理恢复功能
                int readerId = Integer.parseInt(request.getParameter("id"));
                if (dao.restoreReader(readerId)) {
                    request.setAttribute("success", "读者账户已成功恢复");
                } else {
                    request.setAttribute("error", "恢复读者账户失败");
                }
            } else if ("add".equals(action)) {
                request.getRequestDispatcher("readerEdit.jsp").forward(request, response);
                return;
            }

            // 显示读者
            List<Reader> list;
            if (search != null && !search.trim().isEmpty()) {
                list = dao.findReadersByKeyword(search.trim());
            } else {
                list = dao.getAllReaders();
            }
            request.setAttribute("readers", list);
        } catch (Exception e) {
            request.setAttribute("error", "操作失败: " + e.getMessage());
        }
        request.getRequestDispatcher("readerManage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("readerId");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String genderStr = request.getParameter("gender");
        String contact = request.getParameter("contact");
        String maxBorrowStr = request.getParameter("maxBorrow");
        String statusStr = request.getParameter("status");

        Reader r = new Reader();
        r.setUsername(username);
        r.setPassword(password);
        r.setName(name);
        r.setGender(genderStr == null || genderStr.isEmpty() ? null : genderStr.substring(0, 1));
        r.setContact(contact);
        r.setMaxBorrow(maxBorrowStr == null || maxBorrowStr.isEmpty() ? 5 : Integer.parseInt(maxBorrowStr));
        r.setStatus(statusStr == null || statusStr.isEmpty() ? "A" : statusStr.substring(0, 1));

        try {
            if (idStr != null && !idStr.isEmpty()) {
                r.setReaderId(Integer.parseInt(idStr));
                if (password != null && !password.isEmpty()) {
                    // 如果填写了密码，则调用原有updateReader（会更新密码）
                    dao.updateReader(r);
                } else {
                    // 没填密码，仅更新其他字段
                    dao.updateReaderWithoutPassword(r);
                }
            } else {
                // 新增必须有密码
                if (dao.isUsernameExists(username)) throw new Exception("用户名已存在");
                if (dao.isContactExists(contact)) throw new Exception("联系方式已存在");
                if (password == null || password.isEmpty()) throw new Exception("密码不能为空");
                dao.createReader(r);
            }
            response.sendRedirect("readerManage");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("reader", r);
            request.getRequestDispatcher("readerEdit.jsp").forward(request, response);
        }
    }
}