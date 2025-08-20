<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>로그인</title>
</head>
<body>
<h1>로그인</h1>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<form action="front?command=login" method="post">
    <p>
        <label>아이디:</label>
        <input type="text" name="username" required>
    </p>
    <p>
        <label>비밀번호:</label>
        <input type="password" name="password" required>
    </p>
    <p>
        <input type="submit" value="로그인">
        <a href="front?command=signup">회원가입</a>
    </p>
</form>

<p>
    <a href="front?command=boardList">← 게시판으로 돌아가기</a>
</p>
</body>
</html>