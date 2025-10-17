<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Model2 MVC Shop</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css">
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script type="text/javascript">
        $(function() {
            // 로고 클릭 시 메인으로 이동
            $(".logo").on("click", function() {
                if (window.parent) {
                    window.parent.location.href = "/index.jsp";
                }
            });

            // 로그인 버튼 클릭 (기존 기능 유지)
            $("#loginBtn").on("click", function() {
                $(window.parent.frames["rightFrame"].document.location).attr("href", "/user/login");
            });

            // 로그아웃 버튼 클릭 (기존 기능 유지)
            $("#logoutBtn").on("click", function() {
                $(window.parent.document.location).attr("href", "/user/logout");
            });
        });
    </script>
</head>
<body class="top-bar-body">
    <div class="top-container">
        <div class="logo">
            <h1>Model2 MVC Shop</h1>
        </div>
        <nav class="top-nav">
            <c:if test="${ empty user }">
                <a id="loginBtn">LOGIN</a>
            </c:if>
            <c:if test="${ !empty user }">
                <span><strong>${user.userName}</strong>님 💕</span>
                <a id="logoutBtn">LOGOUT</a>
            </c:if>
        </nav>
    </div>
</body>
</html>