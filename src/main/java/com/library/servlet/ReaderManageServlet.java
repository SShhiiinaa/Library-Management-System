package com.library.servlet;

import com.library.dao.ReaderDAO;
import com.library.model.Reader;
import com.library.util.InputSanitizer;
import com.library.util.OperationLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/readerManage")
public class ReaderManageServlet extends HttpServlet {
    private ReaderDAO dao = new ReaderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = InputSanitizer.sanitizeIdentifier(request.getParameter("action"), 20);
        String idStr = InputSanitizer.sanitizeIdentifier(request.getParameter("id"), 20);
        String search = InputSanitizer.sanitizeText(request.getParameter("search"), 50);

        try {
            if ("edit".equals(action) && idStr != null) {
                Reader reader = dao.getReaderById(Integer.parseInt(idStr));
                request.setAttribute("reader", reader);
                request.getRequestDispatcher("readerEdit.jsp").forward(request, response);
                return;
            } else if ("delete".equals(action)) {
                int readerId = Integer.parseInt(idStr);
                if (dao.softDeleteReader(readerId)) {
                    request.setAttribute("success", "读者已标记为删除");
                    OperationLogger.log(request, "delete_reader", String.valueOf(readerId), "success", "管理员标记删除读者ID:" + readerId);
                } else {
                    request.setAttribute("error", "删除读者失败");
                    OperationLogger.log(request, "delete_reader", String.valueOf(readerId), "fail", "管理员标记删除读者失败, ID:" + readerId);
                }
            } else if ("disable".equals(action)) {
                int readerId = Integer.parseInt(idStr);
                if (dao.disableReader(readerId)) {
                    request.setAttribute("success", "读者账户已被禁用");
                    OperationLogger.log(request, "disable_reader", String.valueOf(readerId), "success", "管理员禁用读者ID:" + readerId);
                } else {
                    request.setAttribute("error", "禁用读者失败");
                    OperationLogger.log(request, "disable_reader", String.valueOf(readerId), "fail", "管理员禁用读者失败, ID:" + readerId);
                }
            } else if ("restore".equals(action)) {
                int readerId = Integer.parseInt(idStr);
                if (dao.restoreReader(readerId)) {
                    request.setAttribute("success", "读者账户已成功恢复");
                    OperationLogger.log(request, "restore_reader", String.valueOf(readerId), "success", "管理员恢复读者ID:" + readerId);
                } else {
                    request.setAttribute("error", "恢复读者账户失败");
                    OperationLogger.log(request, "restore_reader", String.valueOf(readerId), "fail", "管理员恢复读者失败, ID:" + readerId);
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
            request.setAttribute("error", "操作失败: " + InputSanitizer.sanitizeForHtml(e.getMessage()));
            OperationLogger.log(request, "reader_manage", action, "fail", "操作异常: " + e.getMessage());
        }
        request.getRequestDispatcher("readerManage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = InputSanitizer.sanitizeIdentifier(request.getParameter("readerId"), 20);
        String username = InputSanitizer.sanitizeIdentifier(request.getParameter("username"), 20);
        String password = request.getParameter("password");
        String name = InputSanitizer.sanitizeText(request.getParameter("name"), 20);
        String genderStr = InputSanitizer.sanitizeIdentifier(request.getParameter("gender"), 1);
        String contact = InputSanitizer.sanitizeMobile(request.getParameter("contact"));
        String maxBorrowStr = InputSanitizer.sanitizeIdentifier(request.getParameter("maxBorrow"), 2);
        String statusStr = InputSanitizer.sanitizeIdentifier(request.getParameter("status"), 1);

        Reader r = new Reader();
        r.setUsername(username);
        r.setPassword(password);
        r.setName(name);
        r.setGender(genderStr == null || genderStr.isEmpty() ? null : genderStr.substring(0, 1));
        r.setContact(contact);
        try {
            int maxBorrow = (maxBorrowStr == null || maxBorrowStr.isEmpty()) ? 5 : Integer.parseInt(maxBorrowStr);
            r.setMaxBorrow(maxBorrow);
        } catch (NumberFormatException e) {
            r.setMaxBorrow(5);
        }
        r.setStatus(statusStr == null || statusStr.isEmpty() ? "A" : statusStr.substring(0, 1));

        try {
            if (idStr != null && !idStr.isEmpty()) {
                r.setReaderId(Integer.parseInt(idStr));
                if (password != null && !password.isEmpty()) {
                    dao.updateReader(r);
                    OperationLogger.log(request, "update_reader", username, "success", "管理员更新读者(含密码)：" + username);
                } else {
                    dao.updateReaderWithoutPassword(r);
                    OperationLogger.log(request, "update_reader", username, "success", "管理员更新读者(不含密码)：" + username);
                }
            } else {
                if (dao.isUsernameExists(username)) throw new Exception("用户名已存在");
                if (dao.isContactExists(contact)) throw new Exception("联系方式已存在");
                if (password == null || password.isEmpty()) throw new Exception("密码不能为空");
                dao.createReader(r);
                OperationLogger.log(request, "create_reader", username, "success", "管理员新增读者：" + username);
            }
            response.sendRedirect("readerManage");
        } catch (Exception e) {
            request.setAttribute("error", InputSanitizer.sanitizeForHtml(e.getMessage()));
            request.setAttribute("reader", r);
            // 记录日志
            String opType = (idStr != null && !idStr.isEmpty())
                    ? "update_reader"
                    : "create_reader";
            OperationLogger.log(request, opType, username, "fail", "读者信息保存失败: " + e.getMessage());
            request.getRequestDispatcher("readerEdit.jsp").forward(request, response);
        }
    }
}