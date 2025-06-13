package com.library.servlet;

import com.library.dao.BookDao;
import com.library.model.Book;
import com.library.util.DatabaseUtil;
import com.library.util.InputSanitizer;
import com.library.util.OperationLogger;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.math.BigDecimal;
import com.library.dao.CategoryDao;

@WebServlet("/BookManageServlet")
public class BookManageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = InputSanitizer.sanitizeIdentifier(request.getParameter("action"), 20);
        String isbn = InputSanitizer.sanitizeIdentifier(request.getParameter("isbn"), 20);

        try (Connection conn = DatabaseUtil.getConnection()) {
            BookDao bookDao = new BookDao();

            if ("edit".equals(action) && isbn != null) {
                Book book = null;
                for (Book b : bookDao.getAllBooks(conn)) {
                    if (isbn.equals(b.getIsbn())) {
                        book = b;
                        break;
                    }
                }
                if (book != null) {
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "未找到指定图书");
                    OperationLogger.log(request, "edit_book", isbn, "fail", "未找到指定ISBN图书");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
            } else if ("delete".equals(action) && isbn != null) {
                boolean deleted;
                try {
                    deleted = bookDao.deleteBook(conn, isbn);
                } catch (Exception e) {
                    OperationLogger.log(request, "delete_book", isbn, "fail", "数据库异常：" + e.getMessage());
                    throw e;
                }
                if (!deleted) {
                    request.setAttribute("error", "删除图书失败");
                    OperationLogger.log(request, "delete_book", isbn, "fail", "删除图书失败");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
                OperationLogger.log(request, "delete_book", isbn, "success", "删除图书成功");
                response.sendRedirect("BookManageServlet");
                return;
            }

            request.setAttribute("books", bookDao.getAllBooks(conn));
            request.getRequestDispatcher("/bookManage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取图书列表失败");
            OperationLogger.log(request, "book_manage", null, "fail", "获取图书列表失败：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = InputSanitizer.sanitizeIdentifier(request.getParameter("action"), 10);

        int categoryId = 0;
        String categoryIdStr = InputSanitizer.sanitizeIdentifier(request.getParameter("categoryId"), 10);
        try {
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "分类ID格式不正确");
            request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
            return;
        }
        String categoryName = InputSanitizer.sanitizeText(request.getParameter("categoryName"), 30);

        Book book = new Book();
        book.setIsbn(InputSanitizer.sanitizeIdentifier(request.getParameter("isbn"), 20));
        book.setTitle(InputSanitizer.sanitizeText(request.getParameter("title"), 100));
        book.setAuthor(InputSanitizer.sanitizeText(request.getParameter("author"), 50));
        try {
            book.setRetailPrice(new BigDecimal(request.getParameter("price")));
        } catch (Exception e) {
            request.setAttribute("error", "价格格式错误");
            request.setAttribute("book", book);
            request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
            return;
        }
        try {
            book.setStockQty(Integer.parseInt(InputSanitizer.sanitizeIdentifier(request.getParameter("stock"), 5)));
        } catch (Exception e) {
            request.setAttribute("error", "库存格式错误");
            request.setAttribute("book", book);
            request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
            return;
        }
        book.setCategoryId(categoryId);

        try (Connection conn = DatabaseUtil.getConnection()) {
            CategoryDao categoryDao = new CategoryDao();
            BookDao bookDao = new BookDao();

            if (!categoryDao.categoryExists(conn, categoryId, categoryName)) {
                if (!categoryDao.createCategory(conn, categoryId, categoryName)) {
                    OperationLogger.log(request, "create_category", categoryId + "", "fail", "分类创建失败: " + categoryName);
                    throw new Exception("分类创建失败");
                }
                OperationLogger.log(request, "create_category", categoryId + "", "success", "新增分类: " + categoryName);
            }

            if ("edit".equals(action)) {
                if (bookDao.updateBook(conn, book)) {
                    OperationLogger.log(request, "edit_book", book.getIsbn(), "success", "编辑图书成功");
                    response.sendRedirect("BookManageServlet");
                } else {
                    request.setAttribute("error", "修改图书失败");
                    request.setAttribute("book", book);
                    OperationLogger.log(request, "edit_book", book.getIsbn(), "fail", "编辑图书失败");
                    request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
                }
            } else {
                if (bookDao.addBook(conn, book)) {
                    OperationLogger.log(request, "add_book", book.getIsbn(), "success", "新增图书成功");
                    response.sendRedirect("BookManageServlet");
                } else {
                    request.setAttribute("error", "添加图书失败");
                    request.setAttribute("book", book);
                    OperationLogger.log(request, "add_book", book.getIsbn(), "fail", "新增图书失败");
                    request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "数据库错误: " + e.getMessage());
            request.setAttribute("book", book);
            OperationLogger.log(request, ("edit".equals(action) ? "edit_book" : "add_book"),
                    book.getIsbn(), "fail", "数据库错误: " + e.getMessage());
            request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
        }
    }
}