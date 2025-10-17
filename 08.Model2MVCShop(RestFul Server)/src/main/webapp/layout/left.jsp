<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>Model2 MVC Shop</title>
<link href="/css/left.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function history(){
  window.open("/history.jsp","popWin",
    "left=300, top=200, width=300, height=200, marginwidth=0, marginheight=0, scrollbars=no, scrolling=no, menubar=no, resizable=no");
}
</script>
</head>

<body background="/images/left/imgLeftBg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<c:set var="login" value="${empty sessionScope.user ? sessionScope.userVO : sessionScope.user}" />

<table width="159" border="0" cellspacing="0" cellpadding="0">

  <!-- menu 01 -->
  <tr>
    <td valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="159">
        <c:if test="${!empty login}">
          <tr>
            <td class="Depth03">
              <a href="/user/getUser?userId=${login.userId}" target="rightFrame">개인정보조회</a>
            </td>
          </tr>
        </c:if>

        <c:if test="${!empty login && login.role == 'admin'}">
          <tr>
            <td class="Depth03">
              <a href="/user/listUser" target="rightFrame">회원정보조회</a>
            </td>
          </tr>
        </c:if>

        <tr><td class="DepthEnd">&nbsp;</td></tr>
      </table>
    </td>
  </tr>

  <!-- menu 02 : 관리자 전용 -->
  <c:if test="${!empty login && login.role == 'admin'}">
    <tr>
      <td valign="top">
        <table border="0" cellspacing="0" cellpadding="0" width="159">
          <tr>
            <td class="Depth03">
              <!-- JSP 직접 호출 대신 컨트롤러 GET 매핑 -->
              <a href="/product/addProduct" target="rightFrame">판매상품등록</a>
            </td>
          </tr>
          <tr>
            <td class="Depth03">
              <a href="/product/listProduct?menu=manage" target="rightFrame">판매상품관리</a>
            </td>
          </tr>
          <tr><td class="DepthEnd">&nbsp;</td></tr>
        </table>
      </td>
    </tr>
  </c:if>

  <!-- menu 03 -->
  <tr>
    <td valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="159">
        <tr>
          <td class="Depth03">
            <a href="/product/listProduct?menu=search" target="rightFrame">상 품 검 색</a>
          </td>
        </tr>

        <c:if test="${!empty login && login.role == 'user'}">
          <tr>
            <td class="Depth03">
              <!-- ✅ 경로 수정 -->
              <a href="/purchase/listPurchase" target="rightFrame">구매이력조회</a>
            </td>
          </tr>
        </c:if>

        <tr><td class="DepthEnd">&nbsp;</td></tr>

        <tr>
          <td class="Depth03"><a href="javascript:history()">최근 본 상품</a></td>
        </tr>
      </table>
    </td>
  </tr>

</table>

</body>
</html>
