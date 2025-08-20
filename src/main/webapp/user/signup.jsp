<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
<h1>회원가입</h1>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<form action="front?command=signup" method="post">
    <p>
        <label>아이디:</label>
        <input type="text" name="username" required>
    </p>
    <p>
        <label>비밀번호:</label>
        <input type="password" name="password" required>
    </p>
    <p>
        <label>이름:</label>
        <input type="text" name="name" required>
    </p>
    <p>
        <label>이메일:</label>
        <input type="email" name="email">
    </p>
    <p>
        <input type="submit" value="회원가입">
        <a href="front?command=login">로그인</a>
    </p>
</form>
</body>
</html>