<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/calendar.js"></script>

<html>
<head>
  <meta charset="UTF-8">
  <title><c:out value="상품 수정"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" type="text/css">
</head>

<body bgcolor="#ffffff" text="#000000">
<div style="width:98%; margin-left:10px;">

<c:choose>
  <c:when test="${not empty product}">

  <!-- 타이틀 -->
  <table width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td width="15" height="37">
        <img src="${pageContext.request.contextPath}/images/ct_ttl_img01.gif" width="15" height="37" />
      </td>
      <td background="${pageContext.request.contextPath}/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="93%" class="ct_ttl01"><c:out value="상품 수정"/></td>
          </tr>
        </table>
      </td>
      <td width="14" height="37">
        <img src="${pageContext.request.contextPath}/images/ct_ttl_img03.gif" width="14" height="37" />
      </td>
    </tr>
  </table>

  <!-- 상품 수정 form -->
  <form name="productForm" action="${pageContext.request.contextPath}/updateProduct.do" method="post">

    <!-- 수정 키 -->
    <input type="hidden" name="prodNo" value="${product.prodNo}" />

    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:13px;">
      <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
      <tr>
        <td width="104" class="ct_write">상품명 <img src="${pageContext.request.contextPath}/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
        <td bgcolor="D6D6D6" width="1"></td>
        <td><input type="text" id="prodName" name="prodName" value="${product.prodName}" required /></td>
      </tr>

      <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
      <tr>
        <td width="104" class="ct_write">상품상세정보 <img src="${pageContext.request.contextPath}/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
        <td bgcolor="D6D6D6" width="1"></td>
        <td><textarea id="prodDetail" name="prodDetail" rows="4" cols="70">${product.prodDetail}</textarea></td>
      </tr>

      <tr><td colspan="3" height="1" bgcolor="D6D6D6"></td></tr>
      <tr>
        <td class="ct_write">제조일자 <img src="${pageContext.request.contextPath}/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
        <td bgcolor="D6D6D6"></td>
        <td class="ct_write01">
          <input type="text" name="manuDate" readonly="readonly" class="ct_input_g"
                 style="width: 100px; height: 19px;" maxLength="10"
                 value="${product.manufactureDay}" />
          &nbsp;
          <img src="${pageContext.request.contextPath}/images/ct_icon_date.gif" width="15" height="15"
               onclick="show_calendar('document.productForm.manuDate', document.productForm.manuDate.value)">
        </td>
      </tr>

      <tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
      <tr>
        <td width="104" class="ct_write">가격 <img src="${pageContext.request.contextPath}/images/ct_icon_red.gif" width="3" height="3" align="absmiddle"/></td>
        <td bgcolor="D6D6D6" width="1"></td>
        <td><input type="text" id="price" name="price" value="${product.price}" required /> 원</td>
      </tr>

      <tr><td colspan="3" height="1" bgcolor="D6D6D6"></td></tr>
      <tr>
        <td class="ct_write">상품이미지</td>
        <td bgcolor="D6D6D6"></td>
        <td class="ct_write01">
          <input type="text" name="fileName" class="ct_input_g" value="${product.imageFile}" />
        </td>
      </tr>
    </table>

	<div style="text-align:right; margin-top:10px;">
	  <button type="submit">수정</button>
	  <button type="button" onclick="location.href='${pageContext.request.contextPath}/getProduct.do?prodNo=${product.prodNo}'">취소</button>
	</div>

  </form>
  </c:when>
  <c:otherwise>
    <h3>상품 정보가 없습니다.</h3>
    <a href="${pageContext.request.contextPath}/listProduct.do">목록</a>
  </c:otherwise>
</c:choose>

</div>
</body>
</html>
