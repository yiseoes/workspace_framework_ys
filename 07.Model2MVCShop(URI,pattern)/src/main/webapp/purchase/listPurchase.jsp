<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>구매 내역</title>
  <style>
    body { font-family:sans-serif; }
    .wrap { max-width:1000px; margin:24px auto; }
    table { width:100%; border-collapse:collapse; }
    th, td { padding:8px; border-bottom:1px solid #ddd; text-align:left; }
    .muted { color:#666; font-size:0.95em; }
    .pagination a { margin:0 4px; text-decoration:none; }
    .pagination strong { margin:0 4px; }
  </style>
</head>
<body>
<div class="wrap">
  <h2>구매 내역</h2>

  <c:choose>
    <c:when test="${empty list}">
      <p>구매 내역이 없습니다.</p>
    </c:when>

    <c:otherwise>
      <table>
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

      <!-- 페이징 -->
      <div class="pagination" style="margin-top:12px;">
        <c:set var="curr" value="${empty currentPage ? resultPage.currentPage : currentPage}" />
        <c:set var="ps"   value="${empty pageSize    ? resultPage.pageSize   : pageSize}" />
        <c:set var="sp"   value="${empty startPage   ? resultPage.beginUnitPage : startPage}" />
        <c:set var="ep"   value="${empty endPage     ? resultPage.endUnitPage   : endPage}" />

        <c:forEach var="i" begin="${sp}" end="${ep}">
          <c:choose>
            <c:when test="${i == curr}">
              <strong>[${i}]</strong>
            </c:when>
            <c:otherwise>
              <c:url var="pageUrl" value="/purchase/listPurchase">
                <c:param name="currentPage" value="${i}"/>
                <c:param name="pageSize"    value="${ps}"/>
              </c:url>
              <a href="${pageUrl}">[${i}]</a>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
