<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>구매 내역</title>
  <style>
    body { font-family:sans-serif; }
    .wrap { max-width:1000px; margin:24px auto; }
    table { width:100%; border-collapse:collapse; }
    th, td { padding:8px; border-bottom:1px solid #ddd; text-align:left; }
    .pagination a { margin:0 4px; }
  </style>
</head>
<body>
<div class="wrap">
  <h2>구매 내역</h2>

  <c:set var="list" value="${list}" />
  <c:if test="${empty list}">
    <p>구매 내역이 없습니다.</p>
  </c:if>

  <c:if test="${not empty list}">
    <table>
      <thead>
        <tr>
          <th>거래번호</th>
          <th>상품번호</th>
          <th>상품명</th>
          <th>가격</th>
          <th>거래상태(코드)</th>
          <th>상품상태</th>
          <th>상세</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="p" items="${list}">
          <tr>
            <td>${p.tranNo}</td>
            <td>${p.purchaseProd.prodNo}</td>
            <td>${p.purchaseProd.prodName}</td>
            <td><c:out value="${p.purchaseProd.price}" /></td>
            <td>
              ${p.tranCode}
              <span style="color:#666;">
                <c:choose>
                  <c:when test="${p.tranCode=='1'}">(상품준비중)</c:when>
                  <c:when test="${p.tranCode=='2'}">(배송중)</c:when>
                  <c:when test="${p.tranCode=='3'}">(배송완료)</c:when>
                  <c:otherwise>(상태미정)</c:otherwise>
                </c:choose>
              </span>
            </td>
            <td>${p.purchaseProd.proTranCode}</td>
            <td><a href="${pageContext.request.contextPath}/getPurchase.do?tranNo=${p.tranNo}">보기</a></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <!-- 페이징 -->
    <div class="pagination" style="margin-top:12px;">
      <c:set var="base" value="${base}" />
      <c:if test="${empty base}">
        <c:set var="base" value="${pageContext.request.contextPath}/listPurchase.do?" />
      </c:if>

      <c:forEach var="i" begin="${startPage}" end="${endPage}">
        <c:choose>
          <c:when test="${i == currentPage}">
            <strong>[${i}]</strong>
          </c:when>
          <c:otherwise>
            <a href="${base}page=${i}&pageSize=${pageSize}">[${i}]</a>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>
  </c:if>
</div>
</body>
</html>
