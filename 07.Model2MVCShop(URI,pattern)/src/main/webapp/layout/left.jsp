<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title>Model2 MVC Shop</title>
<link href="/css/left.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
  // window.history와 이름 충돌 피하려고 함수명 변경
  function openHistory(){
    window.open("<c:url value='/history.jsp'/>","popWin",
      "left=300,top=200,width=300,height=200,scrollbars=no,menubar=no,resizable=no");
  }
</script>
</head>

<body background="/images/left/imgLeftBg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="159" border="0" cellspacing="0" cellpadding="0">

  <!-- menu 01 -->
  <tr>
    <td valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="159">
        <c:if test="${not empty user}">
          <tr>
            <td class="Depth03">
              <a href="<c:url value='/user/getUser'><c:param name='userId' value='${user.userId}'/></c:url>" target="rightFrame">
                개인정보조회
              </a>
            </td>
          </tr>
        </c:if>

        <c:set var="__role" value="${empty user ? '' : fn:toLowerCase(user.role)}"/>
        <c:if test="${__role eq 'admin' or __role eq 'role_admin' or __role eq 'a'}">
          <tr>
            <td class="Depth03">
              <a href="<c:url value='/user/listUser'/>" target="rightFrame">회원정보조회</a>
            </td>
          </tr>
        </c:if>

        <tr><td class="DepthEnd">&nbsp;</td></tr>
      </table>
    </td>
  </tr>

  <!-- menu 02 : 관리자 전용 -->
  <c:if test="${__role eq 'admin' or __role eq 'role_admin' or __role eq 'a'}">
    <tr>
      <td valign="top">
        <table border="0" cellspacing="0" cellpadding="0" width="159">
          <tr>
            <td class="Depth03">
              <!-- JSP 직접 열지 말고 컨트롤러 GET 매핑으로 -->
              <a href="<c:url value='/product/addProduct'/>" target="rightFrame">판매상품등록</a>
            </td>
          </tr>
          <tr>
            <td class="Depth03">
              <a href="<c:url value='/product/listProduct'><c:param name='menu' value='manage'/></c:url>" target="rightFrame">
                판매상품관리
              </a>
            </td>
          </tr>
          <tr><td class="DepthEnd">&nbsp;</td></tr>
        </table>
      </td>
    </tr>
  </c:if>

  <!-- menu 03 : 공용 -->
  <tr>
    <td valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="159">
        <tr>
          <td class="Depth03">
            <a href="<c:url value='/product/listProduct'><c:param name='menu' value='search'/></c:url>" target="rightFrame">
              상 품 검 색
            </a>
          </td>
        </tr>

        <c:if test="${not empty user and __role eq 'user'}">
          <tr>
            <td class="Depth03">
              <!-- ★ 07 스타일: /purchase/listPurchase 로 변경 -->
              <a href="<c:url value='/purchase/listPurchase'/>" target="rightFrame">구매이력조회</a>
            </td>
          </tr>
        </c:if>

        <tr><td class="DepthEnd">&nbsp;</td></tr>
        <tr>
          <td class="Depth03"><a href="javascript:openHistory()">최근 본 상품</a></td>
        </tr>
      </table>
    </td>
  </tr>

</table>

</body>
</html>
