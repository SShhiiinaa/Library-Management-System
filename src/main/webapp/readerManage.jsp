<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>读者管理</title>
  <style>
    :root {
      --primary-color: #2c3e50;
      --secondary-color: #3498db;
      --success-color: #2ecc71;
      --error-color: #e74c3c;
      --warning-color: #f39c12; /* 新增：警告色（用于禁用按钮） */
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
    .main-container {
      max-width: 1200px;
      margin: 2.5rem auto;
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    .page-title {
      margin-bottom: 22px;
      text-align: center;
    }
    .reader-top-bar {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      gap: 30px;
      margin-bottom: 16px;
    }
    .search-bar input[type="text"] {
      padding: 5px 8px;
      border: 1px solid #ccc;
      border-radius: 3px;
    }
    .search-bar input[type="submit"], .add-btn {
      padding: 6px 18px;
      background: var(--secondary-color);
      color: #fff;
      border: none;
      border-radius: 4px;
      margin-left: 8px;
      cursor: pointer;
      font-size: 1rem;
      font-weight: 500;
      transition: background 0.2s;
    }
    .add-btn {
      background: var(--success-color);
      margin-left: 0;
    }
    .search-bar input[type="submit"]:hover {
      background: var(--primary-color);
    }
    .add-btn:hover {
      background: var(--secondary-color);
    }
    /* 新增：成功消息样式 */
    .success-msg {
      color: var(--success-color);
      text-align: center;
      margin: 8px 0;
      padding: 8px 0;
      background: #ecffec;
      border-radius: 4px;
    }
    .error-msg {
      color: var(--error-color);
      text-align: center;
      margin: 8px 0;
      padding: 8px 0;
      background: #ffecec;
      border-radius: 4px;
    }
    table {
      border-collapse: collapse;
      width: 100%;
      margin: 20px auto 0 auto;
      background: #fff;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 1px 8px rgba(52,152,219,0.08);
    }
    th, td {
      border: 1px solid #eee;
      padding: 10px;
      text-align: center;
      vertical-align: middle;
    }
    th {
      background-color: var(--primary-color);
      color: #fff;
    }
    .action-btn {
      padding: 4px 12px;
      background: #f3bb1c;
      color: #fff;
      border: none;
      border-radius: 4px;
      margin: 0 2px;
      cursor: pointer;
      font-size: 0.96em;
      transition: background 0.15s;
      min-width: 60px;
    }
    .action-btn:hover {
      background: #e1a106;
    }
    .action-btn.delete {
      background: var(--error-color);
    }
    .action-btn.delete:hover {
      background: #b8322b;
    }
    /* 新增：禁用和恢复按钮样式 */
    .action-btn.disable {
      background: var(--warning-color);
    }
    .action-btn.disable:hover {
      background: #e67e22;
    }
    .action-btn.restore {
      background: var(--success-color);
    }
    .action-btn.restore:hover {
      background: #27ae60;
    }
    .no-readers {
      text-align: center;
      color: #666;
      padding: 2rem;
    }
    /* 新增：状态样式 */
    .status-active {
      color: var(--success-color);
      font-weight: bold;
    }
    .status-inactive {
      color: var(--warning-color);
      font-weight: bold;
    }
    .status-deleted {
      color: #888;
      font-weight: bold;
    }
  </style>
</head>
<body>
<div class="header">
  <div class="top-nav">
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
<div class="main-container">
  <h2 class="page-title">读者管理</h2>
  <div class="reader-top-bar">
    <!-- 搜索读者 -->
    <form class="search-bar" method="get" action="readerManage" style="display:inline;">
      <input type="text" name="search" placeholder="输入用户名或姓名" value="${param.search}" />
      <input type="submit" value="搜索" />
    </form>
    <!-- 新增读者 -->
    <form method="get" action="readerManage" style="display:inline;">
      <input type="hidden" name="action" value="add"/>
      <input type="submit" class="add-btn" value="新增读者" />
    </form>
  </div>

  <!-- 显示成功和错误消息 -->
  <c:if test="${not empty success}">
    <div class="success-msg">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="error-msg">${error}</div>
  </c:if>

  <table>
    <tr>
      <th>ID</th>
      <th>用户名</th>
      <th>姓名</th>
      <th>性别</th>
      <th>联系方式</th>
      <th>可借数</th>
      <th>状态</th>
      <th>操作</th>
    </tr>
    <c:forEach var="r" items="${readers}">
      <tr>
        <td>${r.readerId}</td>
        <td>${r.username}</td>
        <td>${r.name}</td>
        <td>
          <c:choose>
            <c:when test="${r.gender eq 'M'}">男</c:when>
            <c:when test="${r.gender eq 'F'}">女</c:when>
            <c:otherwise>未知</c:otherwise>
          </c:choose>
        </td>
        <td>${r.contact}</td>
        <td>${r.maxBorrow}</td>
        <td>
          <c:choose>
            <c:when test="${r.status eq 'A'}"><span class="status-active">正常</span></c:when>
            <c:when test="${r.status eq 'I'}"><span class="status-inactive">禁用</span></c:when>
            <c:when test="${r.status eq 'D'}"><span class="status-deleted">已删除</span></c:when>
            <c:otherwise>${r.status}</c:otherwise>
          </c:choose>
        </td>
        <td>
          <!-- 编辑按钮总是显示 -->
          <form method="get" action="readerManage" style="display:inline;">
            <input type="hidden" name="action" value="edit"/>
            <input type="hidden" name="id" value="${r.readerId}" />
            <input type="submit" class="action-btn" value="编辑" />
          </form>

          <!-- 根据状态显示不同的操作按钮 -->
          <c:choose>
            <c:when test="${r.status eq 'A'}">
              <!-- 正常状态显示禁用按钮 -->
              <form method="get" action="readerManage" style="display:inline;" onsubmit="return confirm('确认要禁用该读者吗？');">
                <input type="hidden" name="action" value="disable"/>
                <input type="hidden" name="id" value="${r.readerId}" />
                <input type="submit" class="action-btn disable" value="禁用" />
              </form>

              <!-- 正常状态显示删除按钮 -->
              <form method="get" action="readerManage" style="display:inline;" onsubmit="return confirm('确认要删除该读者吗？');">
                <input type="hidden" name="action" value="delete"/>
                <input type="hidden" name="id" value="${r.readerId}" />
                <input type="submit" class="action-btn delete" value="删除" />
              </form>
            </c:when>

            <c:when test="${r.status eq 'I'}">
              <!-- 禁用状态显示恢复按钮 -->
              <form method="get" action="readerManage" style="display:inline;">
                <input type="hidden" name="action" value="restore"/>
                <input type="hidden" name="id" value="${r.readerId}" />
                <input type="submit" class="action-btn restore" value="恢复" />
              </form>

              <!-- 禁用状态也可以删除 -->
              <form method="get" action="readerManage" style="display:inline;" onsubmit="return confirm('确认要删除该读者吗？');">
                <input type="hidden" name="action" value="delete"/>
                <input type="hidden" name="id" value="${r.readerId}" />
                <input type="submit" class="action-btn delete" value="删除" />
              </form>
            </c:when>

            <c:when test="${r.status eq 'D'}">
              <!-- 删除状态显示恢复按钮 -->
              <form method="get" action="readerManage" style="display:inline;">
                <input type="hidden" name="action" value="restore"/>
                <input type="hidden" name="id" value="${r.readerId}" />
                <input type="submit" class="action-btn restore" value="恢复" />
              </form>
            </c:when>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty readers}">
      <tr>
        <td colspan="8" class="no-readers">暂无读者信息</td>
      </tr>
    </c:if>
  </table>
</div>
</body>
</html>