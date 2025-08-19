<%--
  Created by IntelliJ IDEA.
  User: lider
  Date: 25. 8. 19.
  Time: 오후 3:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="front?command=boardUpdate&id=${board.id}" method="post">
  <p>
    <label>제목:</label>
    <input type="text" name="title" value="${board.title}" required>
  </p>
  <p>
    <label>내용:</label>
    <textarea name="content" rows="5" cols="50">${board.content}</textarea>
  </p>
  <p>
    <input type="submit" value="수정 완료">
    <a href="front?command=boardList">목록</a>
  </p>
</form>
</body>
</html>
</body>
</html>
