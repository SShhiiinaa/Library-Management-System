<%@ page import="com.library.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if(user == null || !"ADMIN".equals(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>图书管理系统 - 图书管理</title>
    <style>
        /* ...（样式不变，略）... */
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --success-color: #2ecc71;
            --error-color: #e74c3c;
            --main-dark: #18181A;
            --purple: #7C3AED;
            --neon-green: #06D6A0;
            --offwhite: #F7F6EF;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }
        .header {
            background: linear-gradient(90deg, var(--main-dark) 70%, var(--purple) 100%);
            color: var(--offwhite);
            padding: 1.2rem 0 1rem 0;
            box-shadow: 0 2px 18px rgba(28,32,48,0.12);
            border-bottom-left-radius: 32px;
            border-bottom-right-radius: 32px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .top-nav {
            width: 100%;
            max-width: 1200px;
            display: flex;
            justify-content: flex-end;
            gap: 1.3rem;
            margin: 0 auto;
            position: relative;
        }
        .nav-link {
            color: var(--offwhite);
            background: transparent;
            padding: 0.45em 1.2em;
            border-radius: 16px;
            font-weight: 500;
            text-decoration: none;
            font-family: inherit;
            transition: background 0.15s, color 0.15s;
        }
        .nav-link:hover {
            background: var(--neon-green);
            color: var(--main-dark);
        }
        .home-btn {
            color: var(--main-dark);
            background: var(--neon-green);
            padding: 0.45em 1.2em;
            border-radius: 16px;
            font-weight: 500;
            text-decoration: none;
            font-family: inherit;
            margin-right: auto;
            margin-left: 0;
            border: none;
            cursor: pointer;
            transition: background 0.15s, color 0.15s, transform 0.15s;
            display: flex;
            align-items: center;
            gap: 0.4em;
        }
        .home-btn:hover {
            background: var(--secondary-color);
            color: #fff;
            transform: translateY(-2px) scale(1.05);
        }
        .management-container {
            max-width: 1200px;
            margin: 2.5rem auto;
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .form-title {
            color: var(--primary-color);
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
            margin-bottom: 1.5rem;
        }
        .book-form {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        .form-input {
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }
        .form-button {
            background-color: var(--primary-color);
            color: white;
            border: none;
            padding: 0.8rem 1.5rem;
            border-radius: 4px;
            cursor: pointer;
            transition: background 0.3s;
            align-self: end;
        }
        .form-button:hover {
            background-color: var(--secondary-color);
        }
        .results-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1.5rem;
        }
        .results-table th,
        .results-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .results-table th {
            background-color: var(--primary-color);
            color: white;
        }
        .category-group {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
            grid-column: 1 / -1;
        }
        .error-message {
            color: var(--error-color);
            padding: 1rem;
            background: #ffecec;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .refresh-button {
            display: inline-block;
            margin-bottom: 1.5rem;
            padding: 0.6rem 1.6rem;
            background: var(--secondary-color);
            color: #fff;
            border: none;
            border-radius: 24px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            text-decoration: none;
            box-shadow: 0 2px 8px rgba(52,152,219,0.10);
            transition: background 0.2s, transform 0.15s;
        }
        .refresh-button:hover {
            background: var(--primary-color);
            transform: translateY(-2px) scale(1.05);
        }
        .action-link {
            margin-right: 8px;
            color: var(--secondary-color);
            text-decoration: underline;
            cursor: pointer;
        }
        .action-link.delete {
            color: var(--error-color);
        }
        @media (max-width: 768px) {
            .category-group {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="header">
    <div class="top-nav">
        <!-- 返回首页按钮放最左侧 -->
        <a class="home-btn" href="index.jsp">&larr; 返回首页</a>
        <a class="nav-link" href="search.jsp">图书查询</a>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a class="nav-link" href="bookManage.jsp">图书管理</a>
            <a class="nav-link" href="readerManage">用户管理</a>
        </c:if>
        <c:if test="${sessionScope.user.role == 'USER'}">
            <a class="nav-link" href="borrowRecord">我的借阅</a>
            <a class="nav-link" href="borrow.jsp">图书借阅</a>
            <a class="nav-link" href="returnBooks.jsp">图书归还</a>
        </c:if>
    </div>
</div>
<div class="management-container">
    <h1 class="form-title">📚 图书管理</h1>

    <c:if test="${not empty error}">
        <div class="error-message">操作失败: ${error}</div>
    </c:if>

    <!-- 新增图书表单 -->
    <form action="BookManageServlet" method="post" class="book-form">
        <div class="form-group">
            <input type="text" name="isbn" placeholder="ISBN" class="form-input" required>
        </div>

        <div class="form-group">
            <input type="text" name="title" placeholder="书名" class="form-input" required>
        </div>

        <div class="form-group">
            <input type="text" name="author" placeholder="作者" class="form-input" required>
        </div>

        <div class="category-group">
            <div class="form-group">
                <input type="number" name="categoryId" placeholder="分类ID" class="form-input" required>
            </div>
            <div class="form-group">
                <input type="text" name="categoryName" placeholder="分类名称" class="form-input" required>
            </div>
        </div>

        <div class="form-group">
            <input type="number" step="0.01" name="price" placeholder="价格" class="form-input" min="0.01" required>
        </div>

        <div class="form-group">
            <input type="number" name="stock" placeholder="库存数量" class="form-input" min="0" required>
        </div>

        <button type="submit" class="form-button">添加图书</button>
    </form>
    <div class="action-buttons">
        <a href="BookManageServlet" class="refresh-button">🔄 显示全部图书</a>
    </div>
    <!-- 图书列表 -->
    <table class="results-table">
        <thead>
        <tr>
            <th>ISBN</th>
            <th>书名</th>
            <th>作者</th>
            <th>分类</th>
            <th>价格</th>
            <th>库存</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${books}" var="book">
            <tr>
                <td>${book.isbn}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.categoryName}(ID:${book.categoryId})</td>
                <td>¥${book.retailPrice}</td>
                <td>${book.stockQty}</td>
                <td>
                    <a class="action-link" href="BookManageServlet?action=edit&isbn=${book.isbn}">编辑</a>
                    <a class="action-link delete" href="BookManageServlet?action=delete&isbn=${book.isbn}" onclick="return confirm('确定要删除该图书吗？');">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>