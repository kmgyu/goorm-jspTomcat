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

<div style="text-align: right; margin: 10px;">
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            안녕하세요, <a href="front?command=userinfo">${sessionScope.userName}</a>님!
            <a href="front?command=logout">로그아웃</a>
        </c:when>
        <c:otherwise>
            <a href="front?command=login">로그인</a> |
            <a href="front?command=signup">회원가입</a>
        </c:otherwise>
    </c:choose>
</div>

<!-- 글쓰기 버튼을 로그인한 사용자에게만 표시 -->
<c:if test="${not empty sessionScope.user}">
    <a href="front?command=boardWrite" class="write-btn">글쓰기</a>
</c:if>

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