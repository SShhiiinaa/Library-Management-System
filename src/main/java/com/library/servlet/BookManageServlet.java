package com.library.servlet;

import com.library.dao.BookDao;
import com.library.model.Book;
import com.library.util.DatabaseUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.math.BigDecimal;
import com.library.dao.CategoryDao;

@WebServlet("/BookManageServlet")
public class BookManageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String isbn = request.getParameter("isbn");

        try (Connection conn = DatabaseUtil.getConnection()) {
            BookDao bookDao = new BookDao();

            if ("edit".equals(action) && isbn != null) {
                // 查询指定isbn图书并转发到编辑页面
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
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
            } else if ("delete".equals(action) && isbn != null) {
                boolean deleted = bookDao.deleteBook(conn, isbn);
                if (!deleted) {
                    request.setAttribute("error", "删除图书失败");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
                response.sendRedirect("BookManageServlet");
                return;
            }

            // 默认显示全部
            request.setAttribute("books", bookDao.getAllBooks(conn));
            request.getRequestDispatcher("/bookManage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取图书列表失败");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action"); // 新增action判断

        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String categoryName = request.getParameter("categoryName");

        Book book = new Book();
        book.setIsbn(request.getParameter("isbn"));
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setRetailPrice(new BigDecimal(request.getParameter("price")));
        book.setStockQty(Integer.parseInt(request.getParameter("stock")));
        book.setCategoryId(categoryId);

        try (Connection conn = DatabaseUtil.getConnection()) {
            CategoryDao categoryDao = new CategoryDao();
            BookDao bookDao = new BookDao();

            // 验证或创建分类
            if (!categoryDao.categoryExists(conn, categoryId, categoryName)) {
                if (!categoryDao.createCategory(conn, categoryId, categoryName)) {
                    throw new Exception("分类创建失败");
                }
            }

            if ("edit".equals(action)) {
                // 编辑图书（isbn为主键，不可变，表单隐藏项传递）
                if (bookDao.updateBook(conn, book)) {
                    response.sendRedirect("BookManageServlet");
                } else {
                    request.setAttribute("error", "修改图书失败");
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
                }
            } else {
                // 新增图书
                if (bookDao.addBook(conn, book)) {
                    response.sendRedirect("BookManageServlet");
                } else {
                    request.setAttribute("error", "添加图书失败");
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "数据库错误: " + e.getMessage());
            request.setAttribute("book", book);
            request.getRequestDispatcher("/bookEdit.jsp").forward(request, response);
        }
    }
}