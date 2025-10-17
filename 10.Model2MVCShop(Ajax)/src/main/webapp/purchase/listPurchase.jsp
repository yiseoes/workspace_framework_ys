<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <c:set var="cPath" value="${pageContext.request.contextPath}" />
    <title>구매 내역</title>
    <%-- CSS 파일 연결 (페이지 번호 스타일이 추가되었으니 버전업!) --%>
    <link rel="stylesheet" href="${cPath}/css/product-form.css?v=5" type="text/css">
</head>
<body>
<%-- (스타일 추가) 전체 레이아웃을 감싸는 prod-wrap --%>
<div class="prod-wrap">
    <%-- (스타일 추가) h2 태그를 공통 page-title 스타일로 변경 --%>
    <div class="page-title">구매 내역</div>

    <c:choose>
        <c:when test="${empty list}">
            <p>구매 내역이 없습니다.</p>
        </c:when>
        <c:otherwise>
            <%-- (스타일 추가) table에 list-table 클래스 적용 --%>
            <table class="list-table">
                <thead>
                    <tr>
                        <th>거래번호</th>
                        <th>상품번호</th>
                        <th>상품명</th>
                        <th>가격</th>
                        <th>거래상태</th>
                        <th>상품상태</th>
                        <th>상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${list}">
                        <tr>
                            <td>${p.tranNo}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.purchaseProd}">${p.purchaseProd.prodNo}</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.purchaseProd}">${p.purchaseProd.prodName}</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.purchaseProd and not empty p.purchaseProd.price}">
                                        <fmt:formatNumber value="${p.purchaseProd.price}" type="number"/>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:set var="tcode" value="${fn:trim(p.tranCode)}"/>
                                <c:choose>
                                    <c:when test="${tcode=='1'}">상품준비중 <span class="muted">(1)</span></c:when>
                                    <c:when test="${tcode=='2'}">배송중 <span class="muted">(2)</span></c:when>
                                    <c:when test="${tcode=='3'}">배송완료 <span class="muted">(3)</span></c:when>
                                    <c:otherwise>상태미정
                                        <c:if test="${not empty tcode}">
                                            <span class="muted">(${tcode})</span>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.purchaseProd and not empty p.purchaseProd.proTranCode}">
                                        ${p.purchaseProd.proTranCode}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:url var="detailUrl" value="/purchase/getPurchase">
                                    <c:param name="tranNo" value="${p.tranNo}"/>
                                </c:url>
                                <a href="${detailUrl}">보기</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <%-- (스타일 추가) 페이징 div에 pagination 클래스 적용 --%>
            <div class="pagination">
                <c:set var="curr" value="${empty currentPage ? resultPage.currentPage : currentPage}" />
                <c:set var="ps"   value="${empty pageSize    ? resultPage.pageSize   : pageSize}" />
                <c:set var="sp"   value="${empty startPage   ? resultPage.beginUnitPage : startPage}" />
                <c:set var="ep"   value="${empty endPage     ? resultPage.endUnitPage   : endPage}" />

                <c:forEach var="i" begin="${sp}" end="${ep}">
                    <c:choose>
                        <c:when test="${i == curr}">
                            <strong>${i}</strong>
                        </c:when>
                        <c:otherwise>
                            <c:url var="pageUrl" value="/purchase/listPurchase">
                                <c:param name="currentPage" value="${i}"/>
                                <c:param name="pageSize"    value="${ps}"/>
                            </c:url>
                            <a href="${pageUrl}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>