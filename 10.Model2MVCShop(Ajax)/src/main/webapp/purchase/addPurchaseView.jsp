<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <c:set var="cPath" value="${pageContext.request.contextPath}" />
    <title>구매하기</title>

    <link rel="stylesheet" href="${cPath}/css/product-form.css?v=4" type="text/css">    
    <script type="text/javascript" src="${cPath}/javascript/calendar.js"></script>

</head>
<body>
<div class="prod-wrap">
    <div class="page-title">구매하기</div>

    <c:choose>
        <c:when test="${empty product}">
            <p>상품 정보가 없습니다.</p>
        </c:when>
        <c:otherwise>
            <div class="purchase-prod-info">
                <img src="${cPath}/images/uploadFiles/${product.imageFile}" 
                     alt="${fn:escapeXml(product.prodName)}"
                     onerror="this.onerror=null; this.src='${cPath}/images/no-image.png';" />
                <div class="prod-details">
                    <div class="prod-name">${product.prodName}</div>
                    <div class="prod-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/> 원</div>
                    <div>상품번호: ${product.prodNo}</div>
                </div>
            </div>

            <form action="${cPath}/purchase/addPurchase" method="post" name="purchaseForm">
                <input type="hidden" name="prodNo" value="${product.prodNo}"/>
                
                <div class="form-container">
                    <div class="form-row">
                        <div class="label-col">구매자</div>
                        <div class="input-col">
                            <input type="text" value="${sessionScope.user.userId}" readonly style="background:#eee;"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="label-col">결제방법</div>
                        <div class="input-col">
                            <select name="paymentOption">
                                <option value="1">현금구매</option>
                                <option value="2">신용구매</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row"><div class="label-col">수령인</div><div class="input-col"><input type="text" name="receiverName" value="${sessionScope.user.userName}" required></div></div>
                    <div class="form-row"><div class="label-col">연락처</div><div class="input-col"><input type="text" name="receiverPhone" value="${sessionScope.user.phone}" required></div></div>
                    <div class="form-row"><div class="label-col">배송지</div><div class="input-col"><input type="text" name="dlvyAddr" value="${sessionScope.user.addr}" placeholder="주소" required></div></div>
                    <div class="form-row"><div class="label-col">요청사항</div><div class="input-col"><input type="text" name="dlvyRequest" placeholder="부재 시 문 앞에 놓아주세요."></div></div>

                    <div class="form-row">
                        <div class="label-col">희망배송일</div>
                        <div class="input-col">
                             <div class="input-with-unit">
                                <input type="text" id="dlvyDate" name="dlvyDate" readonly="readonly" style="width:150px;" maxLength="10" placeholder="YYYY-MM-DD">
                                <img src="${cPath}/images/ct_icon_date.gif" width="15" height="15" class="calendar-icon"
                                     onclick="show_calendar('document.purchaseForm.dlvyDate', document.purchaseForm.dlvyDate.value)">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn-primary">구매 신청</button>
                    <button type="button" class="btn-secondary" onclick="location.href='${cPath}/product/getProduct?prodNo=${product.prodNo}'">취소</button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>