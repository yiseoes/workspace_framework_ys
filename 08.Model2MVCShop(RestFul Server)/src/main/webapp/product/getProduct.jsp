<%@ page contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<title>상품상세조회</title>
<link rel="stylesheet" href="/css/admin.css" type="text/css">
</head>

<%-- 페이지 상단 어딘가에 한 번만 --%>
<c:set var="cPath" value="${pageContext.request.contextPath}" />


<body bgcolor="#ffffff" text="#000000">

	<table width="100%" height="37" border="0" cellpadding="0"
		cellspacing="0">
		<tr>
			<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
				width="15" height="37"></td>
			<td background="/images/ct_ttl_img02.gif" width="100%"
				style="padding-left: 10px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="93%" class="ct_ttl01">상품상세조회</td>
						<td width="20%" align="right">&nbsp;</td>
					</tr>
				</table>
			</td>
			<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"
				width="12" height="37" /></td>
		</tr>
	</table>

	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		style="margin-top: 13px;">
		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
			<td width="104" class="ct_write">상품번호 <img
				src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle" /></td>
			<td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">${product.prodNo}</td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
			<td width="104" class="ct_write">상품명 <img
				src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle" /></td>
			<td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">${product.prodName}</td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
		  <td width="104" class="ct_write">
		    상품이미지
		    <img src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle" />
		  </td>
		  <td bgcolor="#D6D6D6" width="1"></td>
		  <td class="ct_write01">
		    <c:choose>
		      <c:when test="${not empty product.imageFile}">
		        <div>
		          <img
		            src="${cPath}/upload/product/${product.imageFile}"
		            alt="상품이미지"
		            style="max-width:200px;height:auto;border:1px solid #eee;padding:2px;"
		            onerror="this.onerror=null; this.style.display='none';" />
		          <div style="margin-top:6px;font-size:12px;color:#666;">
		            ${product.imageFile}
		          </div>
		        </div>
		      </c:when>
		      <c:otherwise>
		        <span style="color:#888;">등록된 이미지가 없어요.</span>
		      </c:otherwise>
		    </c:choose>
		  </td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
			<td width="104" class="ct_write">상품상세정보 <img
				src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle" /></td>
			<td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">${product.prodDetail}</td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
			<td width="104" class="ct_write">제조일자</td>
			<td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">${product.manufactureDay}</td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
			<td width="104" class="ct_write">가격</td>
			<td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">${product.price}</td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>
		<tr>
			<td width="104" class="ct_write">등록일자</td>
			<td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">${product.regDate}</td>
		</tr>

		<tr>
			<td height="1" colspan="3" bgcolor="D6D6D6"></td>
		</tr>

	</table>

<div style="margin-top: 10px; text-align: right;">

  <%-- 판매상태 코드 정규화 --%>
  <c:set var="pcode" value="${empty product.proTranCode ? '' : fn:trim(product.proTranCode)}" />

  <%-- (1) 구매하기 : 관리자 제외, 판매완료가 아닐 때만 노출 --%>
  <c:if test="${not isAdmin}">
    <c:choose>
      <c:when test="${pcode == 'SOLD' or pcode == '판매완료'}">
        <%-- 판매완료 안내: 버튼 대신 비활성 텍스트/버튼 느낌 --%>
        <button type="button" disabled
                style="opacity:.6;cursor:not-allowed;margin-right:8px;">
          판매완료
        </button>
      </c:when>
      <c:otherwise>
        <form action="/purchase/addPurchaseView" method="get"
              style="display:inline; margin-right:8px;">
          <input type="hidden" name="prodNo" value="${product.prodNo}" />
          <button type="submit">구매하기</button>
        </form>
      </c:otherwise>
    </c:choose>
  </c:if>

  <%-- (2) 목록 --%>
  <form action="/product/listProduct" method="get" style="display: inline;">
    <input type="hidden" name="menu" value="${param.menu}" />
    <button type="submit">목록</button>
  </form>

  <%-- (3) 수정 : 관리자만 --%>
  <c:if test="${isAdmin}">
    <form action="/product/updateProduct" method="get"
          style="display: inline; margin-left: 8px;">
      <input type="hidden" name="prodNo" value="${product.prodNo}" />
      <button type="submit">수정</button>
    </form>
  </c:if>

</div>


</body>
</html>
