<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title>상품상세조회</title>
<link rel="stylesheet" href="/css/admin.css" type="text/css">
</head>

<body bgcolor="#ffffff" text="#000000">

<table width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="15" height="37"><img src="/images/ct_ttl_img01.gif" width="15" height="37"></td>
    <td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="93%" class="ct_ttl01">상품상세조회</td>
          <td width="20%" align="right">&nbsp;</td>
        </tr>
      </table>
    </td>
    <td width="12" height="37">
      <img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
    </td>
  </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:13px;">
  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">상품번호 <img src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.prodNo}</td>
  </tr>

  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">상품명 <img src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.prodName}</td>
  </tr>

  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">상품이미지 <img src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.imageFile}</td>
  </tr>

  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">상품상세정보 <img src="/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.prodDetail}</td>
  </tr>
  
  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">제조일자</td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.manufactureDay}</td>
  </tr>

  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">가격</td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.price}</td>
  </tr>

  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
  <tr>
    <td width="104" class="ct_write">등록일자</td>
    <td bgcolor="D6D6D6" width="1"></td>
    <td class="ct_write01">${product.regDate}</td>
  </tr>

  <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>

</table>

<div style="margin-top:10px; text-align:right;">
  <form action="/product/listProduct" method="get" style="display:inline;">
    <input type="hidden" name="menu" value="${param.menu}" />
    <button type="submit">목록</button>
  </form>
  
	<form action="<c:url value='/purchase/addPurchaseView'/>" method="get" style="display:inline;">
	  <input type="hidden" name="prodNo" value="${product.prodNo}" />
	  <input type="hidden" name="menu" value="${param.menu}" />
	  <button type="submit">구매</button>
	</form>


  <c:if test="${isAdmin}">
    <form action="/product/updateProduct" method="get" style="display:inline; margin-left:8px;">
      <input type="hidden" name="prodNo" value="${product.prodNo}" />
      <button type="submit">수정</button>
    </form>
  </c:if>
</div>

</body>
</html>
