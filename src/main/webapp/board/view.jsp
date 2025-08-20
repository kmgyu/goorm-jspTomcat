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
        <!-- 수정/삭제 버튼을 작성자에게만 표시 -->
        <c:if test="${sessionScope.userId == board.writerId}">
        <a href="front?command=boardUpdate&id=${board.id}">수정</a>
        <a href="front?command=boardDelete&id=${board.id}"
           onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
        </c:if>

        <!-- 작성자 정보 표시? writer name 안씀.-->
<%--    <p>작성자: ${board.writerName}</p>--%>
        <a href="front?command=boardList">목록</a>
    </p>
</c:if>
</body>
</html>