<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>编辑图书</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #f8f9fa;
      margin: 0;
      padding: 0;
    }
    .edit-container {
      max-width: 480px;
      margin: 80px auto;
      padding: 2.5em;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 2px 18px rgba(28,32,48,0.10);
    }
    h2 {
      color: #7C3AED;
      margin-bottom: 1.5em;
      text-align: center;
    }
    .form-group {
      margin-bottom: 1.3em;
    }
    label {
      display: block;
      margin-bottom: 0.4em;
      color: #2c3e50;
      font-weight: 500;
    }
    input[type="text"], input[type="number"] {
      width: 100%;
      padding: 0.7em;
      border: 1px solid #ddd;
      border-radius: 6px;
      font-size: 1em;
    }
    .form-actions {
      text-align: center;
      margin-top: 2em;
    }
    .btn {
      background: #7C3AED;
      color: #fff;
      border: none;
      padding: 0.8em 2.1em;
      border-radius: 24px;
      font-size: 1.08em;
      font-weight: 600;
      cursor: pointer;
      box-shadow: 0 2px 9px #7C3AED22;
      transition: background 0.18s, color 0.18s, transform 0.15s;
    }
    .btn:hover {
      background: #06D6A0;
      color: #18181A;
      transform: scale(1.05);
    }
    .back-link {
      display: inline-block;
      margin-top: 1.3em;
      color: #7C3AED;
      text-decoration: underline;
      font-size: 1em;
    }
    .error-message {
      color: #e74c3c;
      background: #ffecec;
      border-radius: 4px;
      padding: 0.7em;
      margin-bottom: 1em;
      text-align: center;
    }
  </style>
</head>
<body>
<div class="edit-container">
  <h2>编辑图书信息</h2>
  <c:if test="${not empty error}">
    <div class="error-message">${error}</div>
  </c:if>
  <form method="post" action="BookManageServlet">
    <input type="hidden" name="action" value="edit"/>
    <div class="form-group">
      <label>ISBN（不可更改）</label>
      <input type="text" name="isbn" value="${book.isbn}" readonly/>
    </div>
    <div class="form-group">
      <label>书名</label>
      <input type="text" name="title" value="${book.title}" required/>
    </div>
    <div class="form-group">
      <label>作者</label>
      <input type="text" name="author" value="${book.author}" required/>
    </div>
    <div class="form-group">
      <label>分类ID</label>
      <input type="number" name="categoryId" value="${book.categoryId}" required/>
    </div>
    <div class="form-group">
      <label>分类名称</label>
      <input type="text" name="categoryName" value="${book.categoryName}" required/>
    </div>
    <div class="form-group">
      <label>价格</label>
      <input type="number" step="0.01" name="price" value="${book.retailPrice}" min="0.01" required/>
    </div>
    <div class="form-group">
      <label>库存数量</label>
      <input type="number" name="stock" value="${book.stockQty}" min="0" required/>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn">保存修改</button>
    </div>
  </form>
  <div class="form-actions">
    <a href="BookManageServlet" class="back-link">← 返回图书管理</a>
  </div>
</div>
</body>
</html>