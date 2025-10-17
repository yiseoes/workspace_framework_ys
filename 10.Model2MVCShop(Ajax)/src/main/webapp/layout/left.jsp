<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menu</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css">
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script type="text/javascript">
        $(function() {
            // <a> 태그가 없는 메뉴 항목(span)도 클릭 가능하도록 처리
            $(".menu-item span").on("click", function() {
                const menuText = $(this).text().trim();
                if (menuText === '개인정보조회') {
                    $(window.parent.frames["rightFrame"].document.location)
                        .attr("href", "/user/getUser?userId=${user.userId}");
                } else if (menuText === '회원정보조회') {
                    $(window.parent.frames["rightFrame"].document.location)
                        .attr("href", "/user/listUser");
                }
            });

            // 최근 본 상품 history 기능 유지
            $("#historyBtn").on("click", function(e) {
                e.preventDefault();
                window.open("/history.jsp", "popWin", "left=300, top=200, width=300, height=200, scrollbars=no");
            });
        });
    </script>
</head>
<body class="left-menu-body">
    <div class="left-menu-container">
        <div class="menu-group">
            <c:if test="${ !empty user }">
                <div class="menu-item"><span>개인정보조회</span></div>
            </c:if>
            <c:if test="${user.role == 'admin'}">
                <div class="menu-item"><span>회원정보조회</span></div>
            </c:if>
        </div>

        <c:if test="${user.role == 'admin'}">
            <div class="menu-group">
                <div class="menu-item">
                    <a href="../product/addProductView.jsp" target="rightFrame">판매상품등록</a>
                </div>
                <div class="menu-item">
                    <a href="/product/listProduct.do?menu=manage" target="rightFrame">판매상품관리</a>
                </div>
            </div>
        </c:if>

        <div class="menu-group">
            <div class="menu-item">
                <a href="/product/listProduct.do?menu=search" target="rightFrame">상품 검색</a>
            </div>

            <!-- ★ 여기만 수정: /listPurchase.do → /purchase/listPurchase -->
            <c:if test="${ !empty user && user.role == 'user'}">
                <div class="menu-item">
                    <a href="/purchase/listPurchase" target="rightFrame">구매이력조회</a>
                </div>
            </c:if>

            <div class="menu-item">
                <a href="#" id="historyBtn">최근 본 상품</a>
            </div>
        </div>
    </div>
</body>
</html>
