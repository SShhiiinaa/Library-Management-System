<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>图书借阅</title>
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
    .success-message {
      color: var(--success-color);
      padding: 1rem;
      background: #eafaf1;
      border-radius: 4px;
      margin-bottom: 1rem;
    }
    .borrow-btn {
      background: var(--secondary-color);
      color: #fff;
      border: none;
      border-radius: 18px;
      padding: 0.5rem 1.2rem;
      font-size: 1rem;
      font-weight: bold;
      cursor: pointer;
      transition: background 0.2s, transform 0.15s;
    }
    .borrow-btn[disabled] {
      background: #ccc;
      color: #fff;
      cursor: not-allowed;
    }
    .borrow-btn:hover:not([disabled]) {
      background: var(--primary-color);
      transform: scale(1.06);
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
      <a class="nav-link" href="userManage">用户管理</a>
    </c:if>
    <c:if test="${sessionScope.user.role == 'USER'}">
      <a class="nav-link" href="borrowRecord">我的借阅</a>
      <a class="nav-link" href="borrow.jsp">图书借阅</a>
      <a class="nav-link" href="returnBooks.jsp">图书归还</a>
    </c:if>
  </div>
</div>
<div class="search-container">
  <!-- 内容区不再需要返回首页按钮 -->
  <h1>📚 图书借阅</h1>

  <c:if test="${not empty error}">
    <div class="error-message">错误: ${error}</div>
  </c:if>
  <c:if test="${not empty success}">
    <div class="success-message">${success}</div>
  </c:if>

  <form action="borrow" method="get" class="search-form">
    <input type="text" name="title" placeholder="按书名搜索" class="search-input" value="${param.title}">
    <input type="text" name="author" placeholder="按作者搜索" class="search-input" value="${param.author}">
    <input type="text" name="isbn" placeholder="按ISBN搜索" class="search-input" value="${param.isbn}">
    <button type="submit" class="search-button">搜索图书</button>
  </form>

  <c:if test="${not empty books}">
    <table class="results-table">
      <thead>
      <tr>
        <th>ISBN</th>
        <th>书名</th>
        <th>作者</th>
        <th>分类</th>
        <th>库存状态</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${books}" var="book">
        <tr>
          <td>${book.isbn}</td>
          <td>${book.title}</td>
          <td>${book.author}</td>
          <td>${book.categoryName}</td>
          <td class="${book.stockQty > 0 ? 'stock-available' : 'stock-out'}">
            <c:choose>
              <c:when test="${book.stockQty > 0}">
                可借 (${book.stockQty})
              </c:when>
              <c:otherwise>
                已借罄
              </c:otherwise>
            </c:choose>
          </td>
          <td>
            <form action="borrow" method="post" onsubmit="return confirm('确认借阅该书吗？');">
              <input type="hidden" name="isbn" value="${book.isbn}">
              <button type="submit" class="borrow-btn"
                      <c:if test="${book.stockQty < 1}">disabled</c:if>>
                <c:choose>
                  <c:when test="${book.stockQty > 0}">借阅</c:when>
                  <c:otherwise>不可借</c:otherwise>
                </c:choose>
              </button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>
</div>
</body>
</html>