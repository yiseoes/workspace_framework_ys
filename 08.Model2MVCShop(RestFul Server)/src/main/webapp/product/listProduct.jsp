<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<title>상품 목록 조회</title>
<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
  // 페이징/검색 트리거
  function fncGetUserList(currentPage) {
    document.getElementById("currentPage").value = currentPage;
    document.detailForm.submit();
  }
</script>
</head>

<!-- 권한/세션 계산 제거: isAdmin은 컨트롤러/인터셉터에서 주입된 값만 사용 -->
<c:set var="isManage" value="${param.menu eq 'manage'}" />

<body bgcolor="#ffffff" text="#000000">
	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" action="/product/listProduct" method="get">

			<!-- Title -->
			<table width="100%" height="37" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
						width="15" height="37" /></td>
					<td background="/images/ct_ttl_img02.gif" width="100%"
						style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">상품 목록</td>
							</tr>
						</table>
					</td>
					<td width="14" height="37"><img src="/images/ct_ttl_img03.gif"
						width="14" height="37" /></td>
				</tr>
			</table>

			<!-- Search -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td align="right"><select name="searchCondition"
						class="ct_input_g" style="width: 80px">
							<option value="0"
								${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>상품명</option>
							<option value="1"
								${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>가격</option>
					</select> <input type="text" name="searchKeyword"
						value="${ ! empty search.searchKeyword ? search.searchKeyword : '' }"
						class="ct_input_g" style="width: 200px; height: 20px"></td>
					<td align="right" width="70">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="17" height="23"><img
									src="/images/ct_btnbg01.gif" width="17" height="23"></td>
								<td background="/images/ct_btnbg02.gif" class="ct_btn01"
									style="padding-top: 3px;"><a
									href="javascript:fncGetUserList('1');">검색</a></td>
								<td width="14" height="23"><img
									src="/images/ct_btnbg03.gif" width="14" height="23"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			<!-- List -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td colspan="9">전체 ${resultPage.totalCount} 건수, 현재
						${resultPage.currentPage} 페이지</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="100">No</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="300">상품명</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">가격</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="180">등록일</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="200">현재상태 / 배송상태</td>
				</tr>
				<tr>
					<td colspan="9" bgcolor="808285" height="1"></td>
				</tr>

				<c:choose>
					<c:when test="${empty list}">
						<tr>
							<td colspan="9" class="ct_no_list">데이터가 없습니다.</td>
						</tr>
					</c:when>

					<c:otherwise>
						<c:set var="i" value="0" />
						<c:forEach var="p" items="${list}">
							<c:set var="i" value="${i+1}" />
							<tr class="ct_list_pop">
								<td align="center">${i}</td>
								<td></td>

								<td align="left">
									<a href="/product/getProduct?prodNo=${p.prodNo}">${p.prodName}</a>
								</td>
								<td></td>

								<td align="right">
									<c:choose>
										<c:when test="${empty p.price}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${p.price}" type="number" />
										</c:otherwise>
									</c:choose>
								</td>
								<td></td>

								<td align="center">
									<c:choose>
										<c:when test="${empty p.regDate}">-</c:when>
										<c:otherwise>
											<fmt:formatDate value="${p.regDate}" pattern="yyyy-MM-dd" />
										</c:otherwise>
									</c:choose>
								</td>
								<td></td>

								<td align="center">
									<%-- 상태값 정규화 : SOLD 또는 '판매완료' 모두 판매완료로 취급 --%>
									<c:set var="pcode" value="${empty p.proTranCode ? '' : fn:trim(p.proTranCode)}" />

									<span class="status">
										<c:choose>
											<c:when test="${pcode == 'SOLD' or pcode == '판매완료'}">판매완료</c:when>
											<c:otherwise>판매중</c:otherwise>
										</c:choose>
									</span>

									<%-- 관리자 & 판매완료일 때만 배송상태 토글 노출 --%>
									<c:if test="${isAdmin and (pcode == 'SOLD' or pcode == '판매완료')}">
										<span style="margin-left:8px;">
											<a href="<c:url value='/purchase/updateTranCodeByProd'>
											             <c:param name='prodNo' value='${p.prodNo}'/>
											             <c:param name='tranCode' value='1'/>
											             <c:param name='menu' value='${param.menu}'/>
											             <c:param name='currentPage' value='${resultPage.currentPage}'/>
											           </c:url>">[1]</a>
											<a href="<c:url value='/purchase/updateTranCodeByProd'>
											             <c:param name='prodNo' value='${p.prodNo}'/>
											             <c:param name='tranCode' value='2'/>
											             <c:param name='menu' value='${param.menu}'/>
											             <c:param name='currentPage' value='${resultPage.currentPage}'/>
											           </c:url>">[2]</a>
											<a href="<c:url value='/purchase/updateTranCodeByProd'>
											             <c:param name='prodNo' value='${p.prodNo}'/>
											             <c:param name='tranCode' value='3'/>
											             <c:param name='menu' value='${param.menu}'/>
											             <c:param name='currentPage' value='${resultPage.currentPage}'/>
											           </c:url>">[3]</a>
										</span>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="9" bgcolor="D6D7D6" height="1"></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</table>

			<!-- Pager -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td align="center">
						<input type="hidden" id="currentPage" name="currentPage" value="" />
						<jsp:include page="../common/pageNavigator.jsp" />
					</td>
				</tr>
			</table>

		</form>

		<c:set var="cPath" value="${pageContext.request.contextPath}" />

		<c:if test="${isAdmin}">
		  <!-- 관리자 전용 등록 버튼 -->
		  <form action="addProductView.jsp?menu=manage" method="get" class="add-btn-wrap">
		    <button type="submit" class="btn-add">등록</button>
		  </form>
		</c:if>

	</div>
</body>
</html>
