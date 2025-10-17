<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:set var="cPath" value="${pageContext.request.contextPath}" />
    <title>구매 상세 정보</title>
    <link rel="stylesheet" href="${cPath}/css/product-form.css?v=4" type="text/css">
</head>
<body>
<div class="prod-wrap">
    <div class="page-title">구매 상세 정보</div>

    <c:if test="${empty purchase}">
        <p>구매 정보가 없습니다.</p>
    </c:if>

    <c:if test="${not empty purchase}">
        <table class="detail-table">
            <tr><th>거래번호</th><td>${purchase.tranNo}</td></tr>
            <tr>
                <th>거래상태</th>
                <td class="code-emphasis">
                    <c:choose>
                        <c:when test="${purchase.tranCode=='1'}">상품준비중</c:when>
                        <c:when test="${purchase.tranCode=='2'}">배송중</c:when>
                        <c:when test="${purchase.tranCode=='3'}">배송완료</c:when>
                        <c:otherwise>상태미정</c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr><th>구매자 ID</th><td>${purchase.buyer.userId}</td></tr>
            <tr><th>상품명</th><td>${purchase.purchaseProd.prodName}</td></tr>
            
		<tr style="background-color: #fffacd;">
		    <th>[★★★★★★디버깅]</th>
		    <td>
		        값 그냥 찍어보기: [${purchase.paymentOption}]
		    </td>
		</tr>

<tr>
    <th>결제수단</th>
    <td>
        <%-- 어떤 타입의 값이 와도 글자로 강제 변환해서 비교하도록 수정 --%>
        <c:set var="paymentOptionString" value="${purchase.paymentOption}" />
        <c:choose>
            <c:when test="${paymentOptionString == '1'}">현금구매</c:when>
            <c:when test="${paymentOptionString == '2'}">신용구매</c:when>
            <c:otherwise>확인불가</c:otherwise>
        </c:choose>
    </td>
</tr>


			<tr>
			    <th>결제수단</th>
			    <td>
			        <c:set var="paymentOptionString" value="${purchase.paymentOption}" />
			        <c:choose>
			            <c:when test="${fn:contains(paymentOptionString, '1')}">현금구매 (contains로 찾음)</c:when>
			            <c:when test="${fn:contains(paymentOptionString, '2')}">신용구매 (contains로 찾음)</c:when>
			            <c:otherwise>확인불가</c:otherwise>
			        </c:choose>
			    </td>
			</tr>


			<tr>
			    <th>결제수단</th>
			    <td>
			        <%-- [★★★★★★해결함] fn:trim으로 양쪽 공백 제거버전 --%>
			        <c:set var="paymentOptionTrimmed" value="${fn:trim(purchase.paymentOption)}" />
			        <c:choose>
			            <c:when test="${paymentOptionTrimmed == '1'}">현금구매</c:when>
			            <c:when test="${paymentOptionTrimmed == '2'}">신용구매</c:when>
			            <c:otherwise>확인불가</c:otherwise>
			        </c:choose>
			    </td>
			</tr>            
			
			<tr><th>결제수단</th>
			<td>
			<c:if test="${purchase.paymentOption=='1'}">현금구매</c:if>
			<c:if test="${purchase.paymentOption=='2'}">신용구매</c:if>
			</td>
			</tr>
			
            <tr><th>수령인</th><td>${purchase.receiverName}</td></tr>
            <tr><th>연락처</th><td>${purchase.receiverPhone}</td></tr>
            <tr><th>배송지</th><td>${purchase.dlvyAddr}</td></tr>
            <tr><th>요청사항</th><td>${purchase.dlvyRequest}</td></tr>
            <tr><th>배송희망일</th><td>${purchase.dlvyDate}</td></tr>
        </table>

        <div class="button-group">
            <button type="button" class="btn-primary" onclick="location.href='${cPath}/purchase/updatePurchase?tranNo=${purchase.tranNo}'">구매정보 수정</button>
            <button type="button" class="btn-secondary" onclick="location.href='${cPath}/purchase/listPurchase'">내 구매목록</button>
        </div>
    </c:if>
</div>
</body>
</html>