<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<%
  // 기능은 그대로, 스타일만 맞춰주는 버전
%>
<html>
<head>
  <meta charset="UTF-8" />
  <c:set var="cPath" value="${pageContext.request.contextPath}" />

  <!-- 동일 CSS -->
  <link rel="stylesheet" href="${cPath}/css/admin.css" type="text/css" />

  <!-- jQuery 2.1.4 (프로젝트 로컬 경로 기준 유지) -->
  <script src="${cPath}/javascript/jquery-2.1.4.js"></script>

  <!-- 공통 유틸(JS) : 검색 폼 유효성만 사용 -->
  <script src="${cPath}/javascript/CommonScript.js"></script>

  <title>상품 목록</title>

  <style>
    /* 줄무늬는 빼고(요청사항), hover만 은은하게 */
    .prod-wrap { padding: 8px 10px; }
    .prod-list-head { background:#E3E3E3; height:32px; line-height:32px; }
    .prod-row { border-bottom:1px solid #ECECEC; }
    .prod-row:hover { background:#FAFAFA; }
    .prod-name a { text-decoration:none; }
    .status-pill {
      display:inline-block; padding:2px 8px; border-radius:10px; font-size:9pt;
      border:1px solid #CECFCE; background:#F5F5F5; color:#004D73;
    }
    .status-sold { color:#8a0000; border-color:#d9b0b0; background:#fff6f6; }
    .thumb { width:64px; height:64px; object-fit:cover; border:1px solid #EEE; }
    .add-btn-wrap { margin:10px 0; text-align:right; }
    .btn-add { padding:6px 12px; border:1px solid #A5C7F7; background:#FFFFFF; cursor:pointer; }
    .btn-add:hover { background:#F3F8FF; }
    .search-wrap { background:#F5F5F5; padding:8px; margin:10px 0; }
    .search-wrap select, .search-wrap input[type="text"] { height:24px; }
    .price { white-space:nowrap; }
    .ct_list_no td { padding:8px 6px; vertical-align:middle; }
  </style>

  <!-- ★ 네비게이터가 호출하는 표준 함수: fncGetUserList() 추가 -->
  <script type="text/javascript">
    function fncGetUserList(currentPage){
      // detailForm(네비 전용 이름) 우선 탐색, 없으면 searchForm/id 로 대체
      var form =
        document.forms["detailForm"] ||
        document.getElementById("detailForm") ||
        document.forms["searchForm"] ||
        document.getElementById("searchForm") ||
        document.querySelector("form");

      if(!form){
        // 폼이 정말 없으면 쿼리스트링으로 강제 이동
        var qs = new URLSearchParams(location.search);
        qs.set("currentPage", currentPage || 1);
        location.href = location.pathname + "?" + qs.toString();
        return;
      }

      // hidden currentPage 확보/세팅
      var cp = form.querySelector('input[name="currentPage"]');
      if(!cp){
        cp = document.createElement("input");
        cp.type = "hidden";
        cp.name = "currentPage";
        cp.id   = "currentPage";
        form.appendChild(cp);
      }
      cp.value = currentPage || 1;

      // GET + 현재 action 유지(네 파일은 ${cPath}/listProduct.do)
      form.method = "GET";
      form.submit();
    }
  </script>
  <!-- // ★ 끝 -->
  
  <script type="text/javascript">
    $(function(){

      // 행 전체 클릭 시 상세 이동 (a 링크 우선, preventDefault 사용 X : 기본 동작 유지)
      $('.prod-table').on('click', '.prod-row', function(e){
        if($(e.target).closest('a').length) { return; }
        var href = $(this).data('href');
        if(href){ window.location.href = href; }
      });

      // 호버 시 커서 표시
      $('.prod-table').on('mouseenter', '.prod-row', function(){
        $(this).css('cursor','pointer');
      }).on('mouseleave', '.prod-row', function(){
        $(this).css('cursor','default');
      });

      // 검색 폼: CommonScript.js의 FormValidation 사용
      $('#searchForm').on('submit', function(){
        return FormValidation(this);
      });

      // 페이지 크기 변경 -> 1페이지로 이동
      $('#pageSize').on('change', function(){
        $('input[name=currentPage]').val(1);
        $('#searchForm').submit();
      });
    });
  </script>
</head>

<body>

<div class="ct_center_margin prod-wrap">

  <!-- 상단 타이틀 -->
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td class="ct_ttl03">상품 목록</td></tr>
  </table>

  <!-- 관리자: 등록버튼 (기존 흐름 유지) -->
  <c:if test="${isAdmin}">
    <form action="${cPath}/addProductView.do" method="get" class="add-btn-wrap">
      <input type="hidden" name="menu" value="${param.menu}" />
      <button type="submit" class="btn-add">등록</button>
    </form>
  </c:if>

  <!-- 검색 영역 (listUser.jsp 스타일 맞춤: 회색 박스 + select/text) -->
  <!-- ★ name="detailForm" 추가(네비게이터가 찾는 폼 이름) + id 유지 -->
  <form id="searchForm" name="detailForm" action="${cPath}/listProduct.do" method="get" class="search-wrap ct_write01">
    <input type="hidden" name="menu" value="${param.menu}" />
    <!-- ★ currentPage에 id 부여(네비 함수에서 탐색 편의) -->
    <input type="hidden" id="currentPage" name="currentPage" value="${!empty search.currentPage ? search.currentPage : 1}" />

    <select name="searchCondition" class="hp_input_g" fieldTitle="검색조건" required="required">
      <option value="0" <c:if test="${search.searchCondition == 0}">selected</c:if>>상품명</option>
      <option value="1" <c:if test="${search.searchCondition == 1}">selected</c:if>>상세</option>
      <option value="2" <c:if test="${search.searchCondition == 2}">selected</c:if>>상품번호</option>
    </select>

    <input type="text" name="searchKeyword"
           value="${fn:escapeXml(search.searchKeyword)}"
           class="hp_input" style="width:220px;"
           fieldTitle="검색어" minLength="0" />

    <select id="pageSize" name="pageSize" class="hp_input_g" fieldTitle="페이지크기" required="required">
      <option value="3"  <c:if test="${search.pageSize == 3}">selected</c:if>>3</option>
      <option value="5"  <c:if test="${search.pageSize == 5}">selected</c:if>>5</option>
      <option value="10" <c:if test="${search.pageSize == 10}">selected</c:if>>10</option>
    </select>

    <button type="submit" class="btn-add">검색</button>
  </form>
  <!-- // ★ 끝 -->

  <!-- 리스트 헤더 -->
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="prod-table ct_box">
    <colgroup>
      <col style="width:80px" />
      <col />
      <col style="width:120px" />
      <col style="width:110px" />
      <col style="width:120px" />
    </colgroup>

    <tr class="ct_list_b prod-list-head">
      <td align="center">이미지</td>
      <td align="left">상품명 / 상세</td>
      <td align="center">등록일</td>
      <td align="center">상태</td>
      <td align="right" style="padding-right:12px;">가격</td>
    </tr>

    <!-- 데이터 행 -->
    <c:forEach var="p" items="${list}">
      <c:set var="detailHref" value="${cPath}/getProduct.do?prodNo=${p.prodNo}&menu=${param.menu}" />
      <tr class="ct_list_no prod-row" data-href="${detailHref}">
        <td align="center">
          <c:choose>
            <c:when test="${not empty p.imageFile}">
              <img class="thumb" src="${cPath}/uploadFiles/${p.imageFile}" alt="thumb" />
            </c:when>
            <c:otherwise>
              <img class="thumb" src="${cPath}/images/no-image.png" alt="no image" />
            </c:otherwise>
          </c:choose>
        </td>

        <td class="prod-name" align="left">
          <div style="font-weight:bold; color:#104A93;">
            <a href="${detailHref}">
              <c:out value="${p.prodName}" />
            </a>
          </div>
          <div class="ct_ttl02" style="margin-top:4px;">
            <c:out value="${p.prodDetail}" />
          </div>
        </td>

        <td align="center">
          <fmt:formatDate value="${p.regDate}" pattern="yyyy-MM-dd" />
        </td>

        <td align="center">
			<c:choose>
			  <%-- SOLD/판매완료 표시 --%>
			  <c:when test="${p.proTranCode == 'SOLD' || p.proTranCode == '판매완료'}">
			    <span class="status-pill status-sold">판매완료</span>
			  </c:when>
			  <%-- 그 외: 판매중 --%>
			  <c:otherwise>
			    <span class="status-pill">판매중</span>
			  </c:otherwise>
			</c:choose>
        </td>

        <td align="right" class="price" style="padding-right:12px;">
          <fmt:formatNumber value="${p.price}" type="number" />
          원
        </td>
      </tr>
    </c:forEach>

    <!-- 데이터 없을 때 -->
    <c:if test="${empty list}">
      <tr class="ct_list_no">
        <td colspan="5" align="center" style="padding:24px 0;">등록된 상품이 없습니다.</td>
      </tr>
    </c:if>
  </table>

  <!-- 페이지 네비게이터: 기존 include 흐름 유지 -->
  <div class="pagenav" style="margin-top:12px;"><!-- ★ 래퍼 클래스 추가: 다른 스크립트가 네비를 가로채지 않게 영역 식별 -->
    <jsp:include page="../common/pageNavigator.jsp">
      <jsp:param name="totalCount"  value="${resultPage.totalCount}" />
      <jsp:param name="currentPage" value="${resultPage.currentPage}" />
      <jsp:param name="pageUnit"    value="${resultPage.pageUnit}" />
      <jsp:param name="pageSize"    value="${resultPage.pageSize}" />
    </jsp:include>
  </div>

</div>

</body>
</html>
