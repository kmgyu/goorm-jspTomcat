<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글 목록</title>
</head>
<body>
<h1>게시글 목록</h1>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<a href="front?command=boardWrite">글쓰기</a>

<table border="1">
    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일</th>
    </tr>
    <c:forEach var="board" items="${boardList}">
        <tr>
            <td>${board.id}</td>
            <td><a href="front?command=boardView&id=${board.id}">${board.title}</a></td>
            <td>${board.author}</td>
            <td>${board.createdAt}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>