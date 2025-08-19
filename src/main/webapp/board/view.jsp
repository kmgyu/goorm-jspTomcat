<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글 보기</title>
</head>
<body>
<h1>게시글 보기</h1>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
    <a href="front?command=boardList">목록으로</a>
</c:if>

<c:if test="${empty error}">
    <table border="1">
        <tr>
            <th>번호</th>
            <td>${board.id}</td>
        </tr>
        <tr>
            <th>제목</th>
            <td>${board.title}</td>
        </tr>
        <tr>
            <th>작성자</th>
            <td>${board.author}</td>
        </tr>
        <tr>
            <th>작성일</th>
            <td>${board.createdAt}</td>
        </tr>
        <tr>
            <th>내용</th>
            <td>${board.content}</td>
        </tr>
    </table>

    <p>
        <a href="front?command=boardWrite">글쓰기</a>
        <a href="front?command=boardList">목록</a>
    </p>
</c:if>
</body>
</html>