<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--<%--%>
<%--    // 안전을 위해 요청 인코딩--%>
<%--    request.setCharacterEncoding("UTF-8");--%>
<%--%>--%>

<%--<jsp:useBean id="emptyUser" class="java.lang.Object" scope="page" />--%>

<%--<c:set var="ctx" value="${pageContext.request.contextPath}" />--%>
<%--<c:set var="userModel">--%>
<%--    <c:choose>--%>
<%--        <c:when test="${not empty requestScope.user}">${requestScope.user}</c:when>--%>
<%--        <c:when test="${not empty sessionScope.user}">${sessionScope.user}</c:when>--%>
<%--        <c:otherwise>${emptyUser}</c:otherwise>--%>
<%--    </c:choose>--%>
<%--</c:set>--%>

<%--<c:set var="valUsername" value="${not empty param.username ? param.username : (not empty userModel.username ? userModel.username : '')}" />--%>
<%--<c:set var="valName"     value="${not empty param.name     ? param.name     : (not empty userModel.name     ? userModel.name     : '')}" />--%>
<%--<c:set var="valEmail"    value="${not empty param.email    ? param.email    : (not empty userModel.email    ? userModel.email    : '')}" />--%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
</head>
<body>
<h1>마이페이지</h1>

<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 12px;">
        <c:out value="${error}" />
    </div>
</c:if>

<form method="post" action="front?command=userinfo">
    <div>
        <label>아이디(로그인용 사용자명)</label><br/>
        <input type="text" name="username" value="${user.username}" />
    </div>

    <div style="margin-top:8px;">
        <label>새 비밀번호</label><br/>
        <input type="password" name="password" />
        <!-- 비워 두면 백엔드 로직에서 빈 문자열로 저장될 수 있음. 백엔드에서 빈 비밀번호 처리(미변경) 로직 권장 -->
    </div>

    <div style="margin-top:8px;">
        <label>이름</label><br/>
        <input type="text" name="name" value="${user.name}" />
    </div>

    <div style="margin-top:8px;">
        <label>이메일</label><br/>
        <input type="email" name="email" value="${user.email}" />
    </div>

    <div style="margin-top:12px;">
        <label>현재 비밀번호 확인</label><br/>
        <input type="password" name="check_password" required />
    </div>

    <div style="margin-top:12px;">
        <button type="submit">저장</button>
    </div>
</form>

<div style="margin-top:16px;">
    <a href="front?command=boardList">목록으로</a>
</div>

<script>
    // 최소한의 클라이언트 검증 (널/공백 방지). 백엔드 유효성 검증은 별도로 필수.
    document.querySelector('form').addEventListener('submit', function(e){
        var username = (document.querySelector('[name="username"]').value || '').trim();
        var checkpw  = (document.querySelector('[name="check_password"]').value || '').trim();
        if(!username){
            alert('아이디를 입력하세요.');
            e.preventDefault();
            return;
        }
        if(!checkpw){
            alert('현재 비밀번호 확인이 필요합니다.');
            e.preventDefault();
        }
    });
</script>
</body>
</html>
