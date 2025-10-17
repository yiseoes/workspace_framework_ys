<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<c:set var="cPath" value="${pageContext.request.contextPath}" />
<title>상품 상세 정보</title>

<link rel="stylesheet" href="${cPath}/css/product-form.css?v=1"
	type="text/css">
<link rel="stylesheet" href="${cPath}/css/product-detail.css?v=3"
	type="text/css">
<style>
.button-group form {
	margin: 0 4px;
}

.button-group form:first-child {
	margin-left: 0;
}

.button-group form:last-child {
	margin-right: 0;
}
</style>
</head>
<body>
	<div class="prod-wrap">
		<div class="page-title">상품 상세 정보</div>

		<c:choose>
			<c:when test="${not empty product}">
				<div class="detail-content">
					<div class="detail-image-box">
						<c:set var="imgSrc" value="${cPath}/images/no-image.png" />
						<c:if test="${not empty product.imageFile}">
							<c:set var="imgSrc"
								value="${cPath}/images/uploadFiles/${product.imageFile}" />
						</c:if>
						<img src="${imgSrc}" alt="상품이미지" class="main-image"
							onerror="this.onerror=null; this.src='${cPath}/images/no-image.png';" />
					</div>

					<div class="detail-info-box">
						<table class="detail-table">
							<tbody>
								<tr>
									<th>상품명</th>
									<td class="product-name-td">${product.prodName}</td>
								</tr>
								<tr>
									<th>상품 상세 정보</th>
									<td>${product.prodDetail}</td>
								</tr>
								<tr>
									<th>가격</th>
									<td class="product-price-td"><fmt:formatNumber
											value="${product.price}" type="number" /> 원</td>
								</tr>
								<tr>
									<th>상품번호</th>
									<td>${product.prodNo}</td>
								</tr>
								<tr>
									<th>제조일자</th>
									<td>${product.manufactureDay}</td>
								</tr>
								<tr>
									<th>등록일자</th>
									<td><fmt:formatDate value="${product.regDate}"
											pattern="yyyy-MM-dd" /></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<%-- ================= 버튼 그룹 ================= --%>
				<div class="button-group">

					<%-- 판매상태 코드 정규화 --%>
					<c:set var="pcode"
						value="${empty product.proTranCode ? '' : fn:trim(product.proTranCode)}" />

					<%-- (1) 구매하기 버튼: 관리자가 아니고, 판매완료가 아닐 때만 노출 --%>
					<c:if test="${!isAdmin}">
						<c:choose>
							<c:when test="${pcode == 'SOLD' or pcode == '판매완료'}">
								<button type="button" class="btn-secondary" disabled
									style="opacity: .6; cursor: not-allowed;">판매완료</button>
							</c:when>
							<c:otherwise>
								<%-- GET 이동은 a 링크가 안전함. 프레임 유지 위해 target 지정 --%>
								<c:url var="buyUrl" value="/purchase/addPurchaseView">
									<c:param name="prodNo" value="${product.prodNo}" />
								</c:url>
								<a href="${buyUrl}" class="btn-primary" target="rightFrame">구매하기</a>
							</c:otherwise>
						</c:choose>
					</c:if>

					<%-- 안전한 currentPage 계산 : 비어있으면 1로 보정 --%>
					<c:set var="safeCurrentPage"
						value="${empty param.currentPage ? 1 : param.currentPage}" />

					<%-- 목록 URL 조립(절대경로 + 인코딩) --%>
					<c:url var="listUrl" value="/product/listProduct">
						<c:param name="menu" value="${param.menu}" />
						<c:param name="searchCondition" value="${param.searchCondition}" />
						<c:param name="searchKeyword" value="${param.searchKeyword}" />
						<c:param name="currentPage" value="${safeCurrentPage}" />
					</c:url>

					<a href="${listUrl}" class="btn-secondary" target="rightFrame">목록</a>


					<%-- (3) 수정 : 관리자 전용, GET은 a 링크로 --%>
					<c:if test="${isAdmin}">
						<c:url var="editUrl" value="/product/updateProduct">
							<c:param name="prodNo" value="${product.prodNo}" />
							<c:param name="searchCondition" value="${param.searchCondition}" />
							<c:param name="searchKeyword" value="${param.searchKeyword}" />
							<c:param name="currentPage" value="${param.currentPage}" />
							<c:param name="view" value="${param.view}" />
						</c:url>
						<a href="${editUrl}" class="btn-primary" target="rightFrame">수정</a>
					</c:if>

				</div>

			</c:when>
			<c:otherwise>
				<p>요청하신 상품 정보를 찾을 수 없습니다.</p>
				<div class="button-group">
					<a href="${cPath}/product/listProduct" class="btn-secondary"
						target="rightFrame">목록으로 돌아가기</a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>
