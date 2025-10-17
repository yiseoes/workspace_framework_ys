<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="product" value="${requestScope.product}" />
<c:set var="login" value="${empty sessionScope.user ? sessionScope.userVO : sessionScope.user}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>구매하기</title>
  <link rel="stylesheet" href="/css/common.css">
  <link rel="stylesheet" href="/css/admin.css">
  <style>
    .panel{max-width:920px;margin:24px auto;padding:20px;border:1px solid #E5E7EB;border-radius:12px;background:#fff}
    .row{display:grid;grid-template-columns:160px 1fr;gap:10px;align-items:center;padding:6px 0}
    .actions{display:flex;gap:8px;margin-top:18px}
    .btn{padding:9px 14px;border-radius:8px;border:1px solid #E5E7EB;background:#F9FAFB;cursor:pointer}
    .btn.primary{background:#16A34A;color:#fff;border-color:#16A34A}
    .prod{display:grid;grid-template-columns:120px 1fr;gap:14px;align-items:center;padding:10px;border:1px solid #E5E7EB;border-radius:10px;background:#F8FAF9}
    .prod img{width:120px;height:90px;object-fit:cover;border-radius:8px;border:1px solid #E5E7EB}
    input[type=text],select{width:100%;padding:8px;border:1px solid #D1D5DB;border-radius:8px}
  </style>
</head>
<body>
<div class="panel">
  <h3>구매하기</h3>

  <c:choose>
    <c:when test="${empty product}">
      <h4>상품정보가 없습니다.</h4>
    </c:when>
    <c:otherwise>
<!--       <div class="prod"> -->
<%--         <img src="/images/${product.fileName}" alt="${fn:escapeXml(product.prodName)}"> --%>
<!--         <div> -->
<%--           <div><strong>${product.prodName}</strong></div> --%>
<%--           <div><fmt:formatNumber value="${product.price}" pattern="#,##0"/> 원</div> --%>
<%--           <div>상품번호 : ${product.prodNo}</div> --%>
<!--         </div> -->
<!--       </div> -->

      <form action="${ctx}/addPurchase.do" method="post" style="margin-top:14px">
        <input type="hidden" name="prodNo" value="${product.prodNo}"/>

        <div class="row">
          <label>구매자</label>
          <div>
            <c:choose>
              <c:when test="${not empty login}">
                ${login.userId}
              </c:when>
              <c:otherwise>-</c:otherwise>
            </c:choose>
          </div>
        </div>

        <div class="row">
          <label>결제방법</label>
          <select name="paymentOption">
            <option value="1">현금구매</option>
            <option value="2">신용구매</option>
          </select>
        </div>

        <div class="row"><label>수령인</label><input type="text" name="receiverName" required></div>
        <div class="row"><label>연락처</label><input type="text" name="receiverPhone" required></div>
        <div class="row"><label>배송지</label><input type="text" name="dlvyAddr" placeholder="주소" required></div>
        <div class="row"><label>요청사항</label><input type="text" name="dlvyRequest" placeholder="부재 시 문 앞에 등"></div>
        <div class="row"><label>희망배송일</label><input type="text" name="dlvyDate" placeholder="YYYYMMDD 혹은 YYYY-MM-DD"></div>

        <div class="actions">
          <button class="btn primary">구매 신청</button>
          <a class="btn" href="${ctx}/getProduct.do?prodNo=${product.prodNo}">상품 상세</a>
          <a class="btn" href="${ctx}/listProduct.do">목록</a>
        </div>
      </form>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
