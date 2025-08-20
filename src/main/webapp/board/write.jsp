<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>글쓰기</title>
</head>
<body>
<h1>글쓰기</h1>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<form action="front?command=boardWrite" method="post">
    <p>
        <label>제목:</label>
        <input type="text" name="title" value="${title}" required>
    </p>
    <p>
        <label>내용:</label>
        <textarea name="content" rows="5" cols="50">${content}</textarea>
    </p>
    <p>
        <input type="submit" value="등록">
        <a href="front?command=boardList">목록</a>
    </p>
</form>
</body>
</html>