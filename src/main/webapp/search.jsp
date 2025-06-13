<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>图书检索系统</title>
    <style>
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
        .search-container {
            max-width: 1200px;
            margin: 2.5rem auto;
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .search-form {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }
        .search-input {
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
        }
        .search-button {
            background-color: var(--primary-color);
            color: white;
            border: none;
            padding: 0.8rem 1.5rem;
            border-radius: 4px;
            cursor: pointer;
            transition: background 0.3s;
        }
        .search-button:hover {
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
        .stock-available {
            color: var(--success-color);
            font-weight: 500;
        }
        .stock-out {
            color: var(--error-color);
            font-weight: 500;
        }
        .no-results {
            text-align: center;
            color: #666;
            padding: 2rem;
        }
        .error-message {
            color: var(--error-color);
            padding: 1rem;
            background: #ffecec;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .back-btn {
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
            box-shadow: 0 2px 8px rgba(52,152,219,0.10);
            transition: background 0.2s, transform 0.15s;
        }
        .back-btn:hover {
            background: var(--primary-color);
            transform: translateY(-2px) scale(1.05);
        }
    </style>
</head>
<body>
<div class="header">
    <div class="top-nav">
        <!-- 返回首页按钮放最左侧 -->
        <a class="home-btn" href="index.jsp">&larr; 返回首页</a>
        <a class="nav-link" href="search.jsp">图书查询</a>
        <c:if test="${not empty sessionScope.user && fn:contains(sessionScope.user.roles, 'ADMIN')}">
            <a class="nav-link" href="bookManage.jsp">图书管理</a>
            <a class="nav-link" href="readerManage">用户管理</a>
        </c:if>
        <c:if test="${not empty sessionScope.user && fn:contains(sessionScope.user.roles, 'USER')}">
            <a class="nav-link" href="borrowRecord">我的借阅</a>
            <a class="nav-link" href="borrow.jsp">图书借阅</a>
            <a class="nav-link" href="returnBooks.jsp">图书归还</a>
        </c:if>
    </div>
</div>
<div class="search-container">
    <!-- 内容区不再需要返回首页按钮 -->
    <h1>🔍 图书检索</h1>
    <c:if test="${not empty error}">
        <div class="error-message">错误: ${error}</div>
    </c:if>
    <form action="search" method="get" class="search-form">
        <input type="text" name="title" placeholder="按书名搜索" class="search-input" value="${param.title}">
        <input type="text" name="author" placeholder="按作者搜索" class="search-input" value="${param.author}">
        <input type="text" name="isbn" placeholder="按ISBN搜索" class="search-input" value="${param.isbn}">
        <select name="category" class="search-input">
            <option value="">所有分类</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.categoryId}" ${param.category == category.categoryId ? 'selected' : ''}>
                        ${category.categoryName}
                </option>
            </c:forEach>
        </select>
        <button type="submit" class="search-button">开始检索</button>
    </form>
    <table class="results-table">
        <thead>
        <tr>
            <th>ISBN</th>
            <th>书名</th>
            <th>作者</th>
            <th>分类</th>
            <th>出版社</th>
            <th>出版日期</th>
            <th>定价</th>
            <th>库存状态</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${books}" var="book">
            <tr>
                <td>${book.isbn}</td>
                <td>${not empty book.title ? book.title : '无书名信息'}</td>
                <td>${not empty book.author ? book.author : '未知作者'}</td>
                <td>${not empty book.categoryName ? book.categoryName : '未分类'}</td>
                <td>${not empty book.publisherName ? book.publisherName : '未知出版社'}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty book.publishDate}">
                            <fmt:formatDate value="${book.publishDate}" pattern="yyyy-MM"/>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">未知日期</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty book.retailPrice}">
                            <span class="price">
                                ¥<fmt:formatNumber value="${book.retailPrice}" pattern="#,##0.00"/>
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">价格待定</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="${book.stockQty gt 0 ? 'stock-available' : 'stock-out'}">
                    <c:choose>
                        <c:when test="${book.stockQty gt 0}">
                            <i class="fa fa-check-circle"></i>
                            可借阅 (${book.stockQty})
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${book.stockQty eq 0}">
                                    <i class="fa fa-times-circle"></i>
                                    已借罄
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-question-circle"></i>
                                    库存异常
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>