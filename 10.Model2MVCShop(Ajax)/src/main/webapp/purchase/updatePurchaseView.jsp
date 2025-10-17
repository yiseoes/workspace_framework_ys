<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty purchase}">
    <script>alert('구매정보가 없습니다.'); history.back();</script>
</c:if>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <c:set var="cPath" value="${pageContext.request.contextPath}" />
    <title>구매정보 수정</title>
    <link rel="stylesheet" href="${cPath}/css/product-form.css?v=4" type="text/css">
    
    <script type="text/javascript" src="${cPath}/javascript/calendar.js"></script>
</head>
<body>
<div class="prod-wrap">
    <div class="page-title">구매정보 수정</div>

    <form action="${cPath}/purchase/updatePurchase" method="post" name="updateForm">
        <input type="hidden" name="tranNo" value="${purchase.tranNo}" />
        
        <div class="form-container">
            <div class="form-row">
                <div class="label-col">구매자 ID</div>
                <div class="input-col"><input type="text" value="${purchase.buyer.userId}" readonly style="background:#eee;"/></div>
            </div>
            <div class="form-row">
                <div class="label-col">구매자 이름</div>
                <div class="input-col"><input type="text" value="${purchase.buyer.userName}" readonly style="background:#eee;"/></div>
            </div>
            <div class="form-row">
                <div class="label-col">결제수단</div>
                <div class="input-col">
                    <select name="paymentOption">
                        <option value="1" ${purchase.paymentOption=='1' ? 'selected' : ''}>현금구매</option>
                        <option value="2" ${purchase.paymentOption=='2' ? 'selected' : ''}>신용구매</option>
                    </select>
                </div>
            </div>
            <div class="form-row">
                <div class="label-col">수령인 이름</div>
                <div class="input-col"><input type="text" name="receiverName" value="${purchase.receiverName}" /></div>
            </div>
            <div class="form-row">
                <div class="label-col">수령인 연락처</div>
                <div class="input-col"><input type="text" name="receiverPhone" value="${purchase.receiverPhone}" /></div>
            </div>
            <div class="form-row">
                <div class="label-col">배송지</div>
                <div class="input-col"><input type="text" name="dlvyAddr" value="${purchase.dlvyAddr}" /></div>
            </div>
            <div class="form-row">
                <div class="label-col">배송 요청사항</div>
                <div class="input-col"><input type="text" name="dlvyRequest" value="${purchase.dlvyRequest}" /></div>
            </div>


            <div class="form-row">
                <div class="label-col">배송 희망일</div>
                <div class="input-col">
                    <div class="input-with-unit">
                        <input type="text" id="dlvyDate" name="dlvyDate" value="${purchase.dlvyDate}" readonly="readonly" style="width:150px;" maxLength="10" placeholder="YYYY-MM-DD">
                        <img src="${cPath}/images/ct_icon_date.gif" width="15" height="15" class="calendar-icon"
                             onclick="show_calendar('document.updateForm.dlvyDate', document.updateForm.dlvyDate.value)">
                    </div>
                </div>
            </div>
        </div>

        <div class="button-group">
            <button type="submit" class="btn-primary">수정 완료</button>
            <button type="button" class="btn-secondary" onclick="location.href='${cPath}/purchase/getPurchase?tranNo=${purchase.tranNo}'">취소</button>
        </div>
    </form>
</div>
</body>
</html>