package com.library.servlet;

import com.library.service.BookService;
import com.library.model.Book;
import com.library.model.Category;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/search")
public class BookSearchServlet extends HttpServlet {
    private BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        try {
            // 获取搜索参数
            String title = req.getParameter("title");
            String author = req.getParameter("author");
            String isbn = req.getParameter("isbn");
            Integer categoryId = parseInteger(req.getParameter("category"));

            // 获取分类列表（用于下拉框）
            List<Category> categories = bookService.getAllCategories();
            req.setAttribute("categories", categories);

            // 执行搜索
            List<Book> books = bookService.searchBooks(title, author, isbn, categoryId);
            req.setAttribute("books", books);

            // 转发到JSP
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search.jsp");
            dispatcher.forward(req, resp);

        } catch (SQLException e) {
            handleError(req, resp, "数据库查询失败：" + e.getMessage());
        } catch (Exception e) {
            handleError(req, resp, "系统错误：" + e.getMessage());
        }
    }

    private Integer parseInteger(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher("/search.jsp").forward(req, resp);
    }
}