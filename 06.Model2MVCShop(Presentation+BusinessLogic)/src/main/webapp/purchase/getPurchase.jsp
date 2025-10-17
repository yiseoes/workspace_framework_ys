<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>구매 상세</title>
  <style>
    body { font-family:sans-serif; }
    .wrap { max-width:800px; margin:24px auto; }
    table { width:100%; border-collapse:collapse; }
    th, td { padding:8px; border-bottom:1px solid #ddd; text-align:left; }
    .btns a { margin-right:8px; }
    .code { font-weight:bold; }
  </style>
</head>
<body>
<div class="wrap">
  <h2>구매 상세</h2>

  <c:if test="${empty purchase}">
    <p>구매 정보가 없습니다.</p>
  </c:if>

  <c:if test="${not empty purchase}">
    <table>
      <tr><th>거래번호</th><td>${purchase.tranNo}</td></tr>
      <tr>
        <th>거래상태(코드)</th>
        <td class="code">
          ${purchase.tranCode}
          <!-- (선택) 화면에서만 한글 라벨 표시 -->
          <span style="color:#666;">
            <c:choose>
              <c:when test="${purchase.tranCode=='1'}"> (상품준비중)</c:when>
              <c:when test="${purchase.tranCode=='2'}"> (배송중)</c:when>
              <c:when test="${purchase.tranCode=='3'}"> (배송완료)</c:when>
              <c:otherwise> (상태미정)</c:otherwise>
            </c:choose>
          </span>
        </td>
      </tr>
      <tr><th>구매자ID</th><td>${purchase.buyer.userId}</td></tr>
      <tr><th>상품번호</th><td>${purchase.purchaseProd.prodNo}</td></tr>
      <tr><th>결제수단</th><td>${purchase.paymentOption}</td></tr>
      <tr><th>수령인</th><td>${purchase.receiverName}</td></tr>
      <tr><th>연락처</th><td>${purchase.receiverPhone}</td></tr>
      <tr><th>배송지</th><td>${purchase.dlvyAddr}</td></tr>
      <tr><th>요청사항</th><td>${purchase.dlvyRequest}</td></tr>
      <tr><th>배송희망일</th><td>${purchase.dlvyDate}</td></tr>
      <tr><th>상품상태</th><td>${purchase.purchaseProd.proTranCode}</td></tr>
    </table>

    <!-- 수동 상태 변경 허용: 액션으로 이동 -->
    <div class="btns" style="margin-top:16px;">
      <a href="${pageContext.request.contextPath}/updatePurchaseView.do?tranNo=${purchase.tranNo}">구매정보 수정</a>
      <span>|</span>
      <a href="${pageContext.request.contextPath}/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=1">상태를 1(상품준비중)로</a>
      <a href="${pageContext.request.contextPath}/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=2">상태를 2(배송중)로</a>
      <a href="${pageContext.request.contextPath}/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=3">상태를 3(배송완료)로</a>
      <span>|</span>
      <a href="${pageContext.request.contextPath}/listPurchase.do">구매목록</a>
    </div>
  </c:if>
</div>
</body>
</html>
