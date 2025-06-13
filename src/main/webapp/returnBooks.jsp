<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>图书归还</title>
  <style>
    :root {
      --primary-color: #2c3e50;
      --secondary-color: #3498db;
      --success-color: #2ecc71;
      --error-color: #e74c3c;
      --background-color: #f8f9fa;
      --main-dark: #18181A;
      --purple: #7C3AED;
      --neon-green: #06D6A0;
      --offwhite: #F7F6EF;
    }
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      margin: 0;
      padding: 0;
      background-color: var(--background-color);
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
    .return-container {
      max-width: 900px;
      margin: 2.5rem auto;
      background: white;
      padding: 2rem 2.5rem;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    h1 {
      margin-top: 0;
      margin-bottom: 1.5rem;
      font-size: 2rem;
      color: var(--primary-color);
      letter-spacing: 1px;
    }
    .msg-success {
      color: var(--success-color);
      padding: 1rem;
      background: #e8faed;
      border-radius: 4px;
      margin-bottom: 1rem;
      font-weight: 500;
    }
    .msg-error {
      color: var(--error-color);
      padding: 1rem;
      background: #ffecec;
      border-radius: 4px;
      margin-bottom: 1rem;
      font-weight: 500;
    }
    .no-records {
      text-align: center;
      color: #666;
      padding: 2rem 0 1.5rem 0;
      font-size: 1.1rem;
    }
    .record-table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 1.5rem;
    }
    .record-table th,
    .record-table td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    .record-table th {
      background-color: var(--primary-color);
      color: white;
      font-weight: 600;
      font-size: 1.04rem;
      letter-spacing: .5px;
    }
    .record-table td {
      vertical-align: top;
    }
    .record-info strong {
      color: var(--secondary-color);
      font-size: 1.08em;
    }
    .record-info em {
      font-style: normal;
      color: #555;
      font-size: .98em;
    }
    .overdue-label {
      color: var(--error-color);
      font-weight: bold;
      margin-left: 8px;
    }
    .btn-return {
      background-color: var(--secondary-color);
      color: white;
      border: none;
      padding: 7px 16px;
      border-radius: 18px;
      font-size: 1em;
      cursor: pointer;
      font-weight: 500;
      transition: background 0.2s, transform 0.15s;
      box-shadow: 0 2px 8px rgba(52,152,219,0.09);
    }
    .btn-return:hover {
      background: var(--primary-color);
      transform: translateY(-2px) scale(1.04);
    }
    @media (max-width: 700px) {
      .return-container {
        padding: 1rem 0.5rem;
      }
      .record-table th, .record-table td {
        padding: 8px;
        font-size: 0.97em;
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
    <c:if test="${not empty sessionScope.user && fn:contains(sessionScope.user.roles, 'ADMIN')}">
      <a class="nav-link" href="bookManage.jsp">图书管理</a>
      <a class="nav-link" href="userManage">用户管理</a>
    </c:if>
    <c:if test="${not empty sessionScope.user && fn:contains(sessionScope.user.roles, 'USER')}">
      <a class="nav-link" href="borrowRecord">我的借阅</a>
      <a class="nav-link" href="borrow.jsp">图书借阅</a>
      <a class="nav-link" href="returnBooks.jsp">图书归还</a>
    </c:if>
  </div>
</div>
<div class="return-container">
  <!-- 内容区不再需要返回首页按钮 -->
  <h1>🔄 图书归还</h1>
  <c:if test="${not empty success}">
    <div class="msg-success">✔️ ${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="msg-error">❌ ${error}</div>
  </c:if>
  <c:choose>
    <c:when test="${empty records}">
      <div class="no-records">暂无未归还图书</div>
    </c:when>
    <c:otherwise>
      <table class="record-table">
        <thead>
        <tr>
          <th style="width:40%;">图书信息</th>
          <th>借阅日期</th>
          <th>应还日期</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="record" items="${records}">
          <tr>
            <td class="record-info">
              <strong>${record.bookTitle}</strong><br>
              <em>${record.bookAuthor}</em><br>
              <span style="color:#888;">ISBN: ${record.isbn}</span>
            </td>
            <td>
              <c:out value="${record.borrowDate}" />
            </td>
            <td>
              <c:out value="${record.dueDate}" />
              <c:if test="${record.overdueDays > 0}">
                <span class="overdue-label">（已逾期）</span>
              </c:if>
            </td>
            <td>
              <form action="returnBooks" method="post" style="display:inline;">
                <input type="hidden" name="recordId" value="${record.recordId}" />
                <button type="submit" class="btn-return">归还</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>