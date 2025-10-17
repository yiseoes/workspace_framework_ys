<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
  <c:when test="${empty purchase}">
    <script>alert('구매정보가 없습니다.'); history.back();</script>
  </c:when>
  <c:otherwise>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>구매정보 수정</title>
<style>
  .panel{max-width:920px;margin:24px auto;padding:20px;border:1px solid #E5E7EB;border-radius:12px;background:#fff}
  .row{display:grid;grid-template-columns:180px 1fr;gap:10px;align-items:center;padding:6px 0}
  .actions{display:flex;gap:8px;margin-top:18px;justify-content:flex-end}
  .btn{padding:9px 14px;border-radius:8px;border:1px solid #E5E7EB;background:#F9FAFB;cursor:pointer}
  .btn.p{background:#EEF6EC}
</style>
</head>
<body>
<div class="panel">
  <h3>구매정보 수정</h3>

  <form action="<c:url value='/purchase/updatePurchase'/>" method="post">
  <input type="hidden" name="tranNo" value="${tranNo}" />
<%--     <input type="hidden" name="tranNo" value="${empty tranNo ? purchase.tranNo : tranNo}" /> --%>

    <div class="row">
      <label>구매자 아이디(회원)</label>
      <input type="text" value="${purchase.buyer.userId}" readonly />
    </div>
    <div class="row">
      <label>구매자 이름(회원)</label>
      <input type="text" value="${purchase.buyer.userName}" readonly />
    </div>

    <div class="row">
      <label>결제수단(거래)</label>
      <select name="paymentOption">
        <option value="1" ${purchase.paymentOption=='1' ? 'selected' : ''}>현금구매</option>
        <option value="2" ${purchase.paymentOption=='2' ? 'selected' : ''}>카드구매</option>
      </select>
    </div>
    <div class="row">
      <label>수령인 이름(거래)</label>
      <input type="text" name="receiverName" value="${purchase.receiverName}" />
    </div>
    <div class="row">
      <label>수령인 연락처(거래)</label>
      <input type="text" name="receiverPhone" value="${purchase.receiverPhone}" />
    </div>
    <div class="row">
      <label>배송지(거래)</label>
      <input type="text" name="dlvyAddr" value="${purchase.dlvyAddr}" />
    </div>
    <div class="row">
      <label>배송 요청사항(거래)</label>
      <input type="text" name="dlvyRequest" value="${purchase.dlvyRequest}" />
    </div>
    <div class="row">
      <label>배송 희망일(거래, YYYYMMDD)</label>
      <input type="text" name="dlvyDate" value="${purchase.dlvyDate}" placeholder="YYYYMMDD" />
    </div>

    <div class="actions">
      <button type="submit" class="btn p">수정</button>
      <a class="btn" href="<c:url value='/purchase/getPurchase'><c:param name='tranNo' value='${purchase.tranNo}'/></c:url>">취소</a>
    </div>
  </form>
</div>
</body>
</html>
  </c:otherwise>
</c:choose>
