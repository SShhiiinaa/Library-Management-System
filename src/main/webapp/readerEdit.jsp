<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑读者</title>
    <style>
        :root { --primary-color: #2c3e50; --secondary-color: #3498db; }
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-box { width: 400px; margin: 30px auto; border: 1px solid #ddd; padding: 24px; border-radius: 8px; }
        .form-box label { display: block; margin-bottom: 6px; color: var(--primary-color); }
        .form-box input, .form-box select { width: 100%; padding: 8px; margin-bottom: 16px; border-radius: 4px; border: 1px solid #ccc; }
        .btn { background-color: var(--secondary-color); color: white; border: none; padding: 8px 22px; border-radius: 4px; cursor: pointer; font-size: 1em; }
        .btn:hover { background-color: #217dbb; }
    </style>
</head>
<body>
<div class="form-box">
    <h2>${reader != null ? "编辑读者" : "新增读者"}</h2>
    <form action="readerManage" method="post">
        <c:if test="${reader != null}">
            <input type="hidden" name="readerId" value="${reader.readerId}" />
        </c:if>

        <label for="username">用户名</label>
        <input type="text" id="username" name="username" required value="${reader.username}" />

        <label for="password">
            密码
            <c:if test="${reader != null}">
                <span style="color:#888;font-size:13px;">（如需修改，请输入新密码；不修改请留空）</span>
            </c:if>
        </label>
        <input type="text" id="password" name="password"
               <c:if test="${reader == null}">required</c:if>
        />

        <label for="name">姓名</label>
        <input type="text" id="name" name="name" required value="${reader.name}" />

        <label for="gender">性别</label>
        <select id="gender" name="gender">
            <option value="M" <c:if test="${reader.gender == 'M'}">selected</c:if>>男</option>
            <option value="F" <c:if test="${reader.gender == 'F'}">selected</c:if>>女</option>
        </select>

        <label for="contact">联系方式</label>
        <input type="text" id="contact" name="contact" required value="${reader.contact}" />

        <label for="maxBorrow">最大可借</label>
        <input type="number" id="maxBorrow" name="maxBorrow" min="1" max="20" value="${reader.maxBorrow != null ? reader.maxBorrow : 5}" />

        <label for="status">状态</label>
        <select id="status" name="status">
            <option value="A" <c:if test="${reader.status == 'A'}">selected</c:if>>正常</option>
            <option value="I" <c:if test="${reader.status == 'I'}">selected</c:if>>禁用</option>
        </select>

        <button type="submit" class="btn">保存</button>
        <a href="readerManage" class="btn" style="background-color: #bbb;">取消</a>
    </form>
    <c:if test="${not empty error}">
        <div style="color:red;">${error}</div>
    </c:if>
</div>
</body>
</html>