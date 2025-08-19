<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 삭제</title>
</head>
<body>
<form action="front?command=boardDelete&id=${id}&confirm=true" method="post">
    <input type="hidden" name="id" value="${id}">
    <p>정말 이 게시글을 삭제하시겠습니까?</p>

    <p>
        <button type="submit">삭제</button>
        <a href="front?command=boardList">취소</a>
    </p>
</form>
</body>
</html>
