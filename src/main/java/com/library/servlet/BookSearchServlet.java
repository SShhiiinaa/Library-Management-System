package com.library.servlet;

import com.library.service.BookService;
import com.library.model.Book;
import com.library.model.Category;
import com.library.util.InputSanitizer;
import com.library.util.OperationLogger;
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

        // 防注入及内容长度过滤
        String title = InputSanitizer.sanitizeText(req.getParameter("title"), 100);
        String author = InputSanitizer.sanitizeText(req.getParameter("author"), 50);
        String isbn = InputSanitizer.sanitizeIdentifier(req.getParameter("isbn"), 20);
        String categoryParam = InputSanitizer.sanitizeIdentifier(req.getParameter("category"), 10);
        Integer categoryId = parseInteger(categoryParam);

        try {
            // 获取分类列表（用于下拉框）
            List<Category> categories = bookService.getAllCategories();
            req.setAttribute("categories", categories);

            // 执行搜索
            List<Book> books = bookService.searchBooks(title, author, isbn, categoryId);
            req.setAttribute("books", books);

            // 日志记录（只记录有搜索条件的情况，防止日志膨胀）
            if ((title != null && !title.isEmpty()) ||
                    (author != null && !author.isEmpty()) ||
                    (isbn != null && !isbn.isEmpty()) ||
                    categoryId != null) {
                OperationLogger.log(req, "search_book",
                        String.format("title=%s,author=%s,isbn=%s,category=%s", title, author, isbn, categoryId),
                        "success",
                        "图书检索"
                );
            }

            // 转发到JSP
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search.jsp");
            dispatcher.forward(req, resp);

        } catch (SQLException e) {
            OperationLogger.log(req, "search_book",
                    String.format("title=%s,author=%s,isbn=%s,category=%s", title, author, isbn, categoryId),
                    "fail",
                    "数据库查询失败：" + e.getMessage()
            );
            handleError(req, resp, "数据库查询失败：" + e.getMessage());
        } catch (Exception e) {
            OperationLogger.log(req, "search_book",
                    String.format("title=%s,author=%s,isbn=%s,category=%s", title, author, isbn, categoryId),
                    "fail",
                    "系统错误：" + e.getMessage()
            );
            handleError(req, resp, "系统错误：" + e.getMessage());
        }
    }

    private Integer parseInteger(String value) {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
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