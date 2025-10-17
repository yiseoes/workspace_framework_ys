<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>구매 완료</title>
  <style>
    body { font-family:sans-serif; }
    .wrap { max-width:720px; margin:24px auto; }
    table { width:100%; border-collapse:collapse; }
    th, td { padding:8px; border-bottom:1px solid #ddd; text-align:left; }
  </style>
</head>
<body>
<div class="wrap">
  <h2>구매가 완료되었습니다</h2>

  <c:set var="p" value="${empty purchase ? purchaseVO : purchase}" />
  <c:if test="${empty p}">
    <p>구매 정보가 없습니다.</p>
  </c:if>

  <c:if test="${not empty p}">
    <table>
      <tr><th>거래번호</th><td>${p.tranNo}</td></tr>
      <tr><th>거래상태(코드)</th><td>${p.tranCode}</td></tr>
      <tr><th>구매자ID</th><td>${p.buyer.userId}</td></tr>
      <tr><th>상품번호</th><td>${p.purchaseProd.prodNo}</td></tr>
      <tr><th>결제수단</th><td>${p.paymentOption}</td></tr>
      <tr><th>수령인</th><td>${p.receiverName}</td></tr>
      <tr><th>연락처</th><td>${p.receiverPhone}</td></tr>
      <tr><th>배송지</th><td>${p.dlvyAddr}</td></tr>
      <tr><th>요청사항</th><td>${p.dlvyRequest}</td></tr>
      <tr><th>배송희망일</th><td>${p.dlvyDate}</td></tr>
    </table>

    <div style="margin-top:16px;">
      <a href="${pageContext.request.contextPath}/getPurchase.do?tranNo=${p.tranNo}">구매 상세 보기</a>
      <span style="margin:0 8px;">|</span>
      <a href="${pageContext.request.contextPath}/listPurchase.do">내 구매내역 보기</a>
    </div>
  </c:if>
</div>
</body>
</html>
