<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <meta charset="UTF-8" />
  <c:set var="cPath" value="${pageContext.request.contextPath}" />

  <script src="${cPath}/javascript/jquery-2.1.4.js"></script>
  <script>
    if (!window.jQuery) {
      document.write('<script src="https://code.jquery.com/jquery-2.1.4.min.js"><\/script>');
    }
  </script>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

  <link rel="stylesheet" href="${cPath}/css/admin.css?v=2" type="text/css" />
  <link rel="stylesheet" href="${cPath}/css/product-list.css?v=1" type="text/css" />

  <script src="${cPath}/javascript/CommonScript.js"></script>

  <title>상품 목록</title>

  <style type="text/css">
    /* 상세 보기 팝업 */
    .product-popover {
      display:none; position:absolute; width:320px; padding:20px;
      background:#fff; border:2px solid #5c89c7; border-radius:12px;
      box-shadow:0 5px 15px rgba(0,0,0,.2); z-index:1000;
      text-align:left; font-size:14px; line-height:1.6;
    }
    .product-popover h4 { margin:0 0 15px; color:#333; font-size:18px; border-bottom:1px solid #eee; padding-bottom:10px; }
    .product-popover p { margin:5px 0; color:#555; }
    .product-popover .price { font-weight:bold; color:#d9534f; }

    /* 배송상태 토글 공통 */
    .tran-toolbar { display:inline-flex; gap:6px; align-items:center; margin-left:6px; }
    .tran-toolbar .tran-btn { display:inline-block; padding:2px 8px; border:1px solid #ddd; border-radius:4px; font-size:12px; background:#fff; }
    .tran-toolbar .tran-btn:hover { background:#f7f7f7; }

    /* 그리드용 카드 래퍼(토글을 카드 아래에 안정적으로 배치) */
    .product-list-grid { display:flex; flex-wrap:wrap; gap:28px 20px; border-top:2px solid #333; padding-top:20px; }
    .product-list-grid > .grid-card { flex:1 1 calc(25% - 20px); min-width:200px; display:flex; flex-direction:column; gap:8px; }
    .grid-card > .product-item-grid { border:1px solid #eee; border-radius:4px; overflow:hidden; transition:box-shadow .2s; display:block; }
    .grid-card > .product-item-grid:hover { box-shadow:0 4px 12px rgba(0,0,0,.08); }
    .tran-toolbar-grid { display:flex; justify-content:center; gap:6px; }
  </style>

<script type="text/javascript">
// 페이징/검색 트리거 (유지)
function fncGetUserList(currentPage){
  var form = document.getElementById("searchForm") || document.forms["searchForm"] || document.forms["detailForm"] || document.querySelector("form");
  if(!form){
    var qs = new URLSearchParams(location.search);
    qs.set("currentPage", currentPage || 1);
    location.href = location.pathname + "?" + qs.toString();
    return;
  }
  var cp = form.querySelector('input[name="currentPage"]');
  if(!cp){
    cp = document.createElement("input");
    cp.type = "hidden"; cp.name = "currentPage"; cp.id = "currentPage";
    form.appendChild(cp);
  }
  cp.value = currentPage || 1;
  form.method = "GET";
  form.submit();
}

$(function(){

  /* 보기 전환 */
  $('#btn-view-list').on('click', function(){
    $('.view-switcher button').removeClass('active');
    $(this).addClass('active');
    $('.product-list-grid').hide();
    $('.product-list-table').show();
    $('input[name="view"]').val('list');
  });
  $('#btn-view-grid').on('click', function(){
    $('.view-switcher button').removeClass('active');
    $(this).addClass('active');
    $('.product-list-table').hide();
    $('.product-list-grid').show();
    $('input[name="view"]').val('grid');
  });

  /* 테이블 행 클릭 → 상세 (내부 a 클릭은 제외) */
  $('.product-list-table').on('click', '.product-item-table', function(e){
    if($(e.target).closest('a').length) { return; }
    var href = $(this).data('href');
    if(href){ window.location.href = href; }
  });

  /* 앨범형 팝업 상세 */
  $('.product-list-grid').on('click', '.product-item-grid', function(event) {
    event.preventDefault();
    var prodNo = $(this).data('prodno');
    if (!prodNo) return;

    $.ajax({
      url: "${cPath}/product/json/getProduct/" + prodNo,
      type: "GET",
      dataType: "json"
    }).done(function(product){
      var regDateText = product.regDate || '정보 없음';
      var content = '<h4>' + product.prodName + '</h4>'
                  + '<p class="price"><strong>가격:</strong> ' + Number(product.price||0).toLocaleString() + ' 원</p>'
                  + '<p><strong>상태:</strong> ' + (product.proTranCode || '판매중') + '</p>'
                  + '<p><strong>상세:</strong> ' + (product.prodDetail || '상세 정보 없음') + '</p>'
                  + '<p><strong>등록일:</strong> ' + regDateText + '</p>';
      $('#product-popover').html(content).css({ left: event.pageX + 15, top: event.pageY + 15 }).show();
    }).fail(function(){
      alert("상세 정보를 불러오는 데 실패했어요");
    });
  });
  $(document).on('click', function(event) {
    if (!$(event.target).closest('.product-item-grid, #product-popover').length) {
      $('#product-popover').hide();
    }
  });

  /* 배송상태 토글 */
  $(document).on('click', '.tran-toolbar .tran-btn', function(e){
    e.preventDefault();
    e.stopPropagation();

    var $btn   = $(this);
    var code   = $btn.data('code');                           // 1/2/3
    var prodNo = $btn.closest('.tran-toolbar').data('prodno');

    var qs = $('#searchForm').serialize();
    if (!qs || qs.indexOf('currentPage=') === -1) {
      var nowPage = '${resultPage.currentPage}' || '1';
      qs += (qs ? '&' : '') + 'currentPage=' + encodeURIComponent(nowPage);
    }
    qs += '&prodNo=' + encodeURIComponent(prodNo) + '&tranCode=' + encodeURIComponent(code);
    window.location.href = '${cPath}/purchase/updateTranCodeByProd' + '?' + qs;
  });

  // =========================
  // 무한 스크롤
  // =========================
  var loading = false;
  var currentPage = Number("${resultPage.currentPage}");
  var pageSize    = Number("${resultPage.pageSize}");
  var totalCount  = Number("${resultPage.totalCount}");
  var totalPage   = Math.max(1, Math.ceil((isNaN(totalCount)?0:totalCount) / (pageSize||1)));

  function buildNextUrl(nextPage){
    // 폼의 원래 action 사용(※ action 강제 변경 금지)
    var base = ($('#searchForm').attr('action') || '').split('?')[0] || location.pathname;
    var params = $('#searchForm').serializeArray();
    var found = false;
    for (var i=0; i<params.length; i++){
      if (params[i].name === 'currentPage'){ params[i].value = String(nextPage); found = true; break; }
    }
    if (!found) params.push({name:'currentPage', value:String(nextPage)});
    var qs = $.param(params);
    return base + '?' + qs;
  }

  function appendRowsFrom(html){
    var $tmp = $('<div>').html(html);
    var $newTableRows = $tmp.find('.product-item-table');
    var $newGridCards = $tmp.find('.product-list-grid > .grid-card');
    var hasData = ($newTableRows.length + $newGridCards.length) > 0;
    if (hasData) {
      $('.product-list-table tbody').append($newTableRows);
      $('.product-list-grid').append($newGridCards);
      return true;
    }
    return false;
  }

  function loadNextPage(){
    if (loading) return;
    if (!isNaN(totalCount) && currentPage >= totalPage) return;
    loading = true;
    var nextPage = (isNaN(currentPage) || currentPage < 1) ? 2 : (currentPage + 1);
    var url = buildNextUrl(nextPage);
    $('.infinite-loading').text('다음 목록을 불러오는 중...').show();
    $.get(url).done(function(html){
      var ok = appendRowsFrom(html);
      if (ok) {
        currentPage = nextPage;
      } else {
        $(window).off('scroll');
        if (observer) observer.disconnect();
        $('.infinite-loading').text('더 이상 데이터가 없습니다.');
      }
      if (!isNaN(totalCount) && currentPage >= totalPage) {
        $(window).off('scroll');
        if (observer) observer.disconnect();
        $('.infinite-loading').hide();
      }
    }).fail(function(xhr){
      $('.infinite-loading').text('요청 실패(' + xhr.status + ')');
    }).always(function(){
      loading = false;
      if (isNaN(totalCount) || currentPage < totalPage) {
        $('.infinite-loading').hide();
      }
    });
  }

  var observer = null;
  var $sentinel = $('.infinite-spacer');
  if ('IntersectionObserver' in window && $sentinel.length) {
    observer = new IntersectionObserver(function(entries){
      entries.forEach(function(entry){ if (entry.isIntersecting) loadNextPage(); });
    }, {root: null, rootMargin: '200px 0px', threshold: 0});
    observer.observe($sentinel.get(0));
  } else {
    $(window).on('scroll', function(){
      if (loading) return;
      if (!isNaN(totalCount) && currentPage >= totalPage) return;
      var nearBottom = $(window).scrollTop() + $(window).height() > $(document).height() - 500;
      if (nearBottom) loadNextPage();
    });
  }

  setTimeout(function(){
    if ($(document).height() <= $(window).height() + 50) {
      loadNextPage();
    }
  }, 80);

  // =========================
  // 오토컴플릿 (0/1/2 지원)
  // =========================
  var $kw   = $('input[name="searchKeyword"]');
  var $cond = $('select[name="searchCondition"]');

  // 폼 submit 유효성만 연결(※ action 강제 변경 금지)
  $('#searchForm').on('submit', function(){ return FormValidation(this); });

  if ($kw.length && $cond.length && $.ui && $.ui.autocomplete) {
    $kw.autocomplete({
      minLength : 1,
      delay     : 150,
      autoFocus : false,
      source    : function(request, response){
        var sc = String($cond.val()); // 0:상품명, 1:상세, 2:상품번호
        $.ajax({
          url     : '${cPath}/product/json/autocomplete',
          dataType: 'json',
          cache   : true,
          data    : { searchCondition: sc, searchKeyword: request.term }
        }).done(function(data){
          response((data && data.items) || []);
        }).fail(function(xhr){
          console.warn('autocomplete xhr fail', xhr.status);
          response([]);
        });
      },
      select : function(e, ui){
        if(ui && ui.item){ $kw.val(ui.item.value); }
        return false;
      },
      focus  : function(e){ e.preventDefault(); }
    });
  } else {
    console.warn('autocomplete guard: target or $.ui not ready');
  }

}); // end of $(function)
</script>

</head>

<body>
<div class="prod-wrap">
  <div class="top-header">
      <div class="page-title">상품 목록</div>
		<div class="view-switcher">
		  <button type="button" id="btn-view-list" class="${empty param.view || param.view == 'list' ? 'active' : ''}">리스트</button>
		  <button type="button" id="btn-view-grid"  class="${param.view == 'grid' ? 'active' : ''}">앨범</button>
		</div>
      
      
  </div>

  <div class="top-controls">
      <form id="searchForm" name="detailForm" action="${cPath}/product/listProduct" method="get" class="search-wrap">
        <input type="hidden" name="menu" value="${param.menu}" />
        <input type="hidden" id="currentPage" name="currentPage" value="${!empty search.currentPage ? search.currentPage : 1}" />
        <input type="hidden" name="view" value="${param.view}" /> 
        <select name="searchCondition" fieldTitle="검색조건" required="required">
          <option value="0" <c:if test="${search.searchCondition == 0}">selected</c:if>>상품명</option>
          <option value="1" <c:if test="${search.searchCondition == 1}">selected</c:if>>상세</option>
          <option value="2" <c:if test="${search.searchCondition == 2}">selected</c:if>>상품번호</option>
        </select>
        <input type="text" name="searchKeyword" value="${fn:escapeXml(search.searchKeyword)}" placeholder="검색어를 입력하세요" fieldTitle="검색어" minLength="0" />
        <button type="submit" class="btn-search">검색</button>
      </form>
      <c:if test="${isAdmin}">
        <form action="${cPath}/product/addProduct" method="get">
          <input type="hidden" name="menu" value="${param.menu}" />
          <button type="submit" class="btn-add">상품 등록</button>
        </form>
      </c:if>
  </div>

  <!-- 리스트(테이블) -->
  <table class="product-list-table">
    <colgroup>
      <col style="width:100px" /><col /><col style="width:120px" /><col style="width:110px" /><col style="width:180px" />
    </colgroup>
    <thead>
      <tr>
        <th>이미지</th><th class="align-left">상품명 / 상세</th><th>등록일</th><th>상태</th><th class="align-right">가격</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="p" items="${list}">
  <%-- 1. 여기서 먼저 완벽한 URL을 만들어서 'detailUrl' 변수에 저장! --%>
  <c:url var="detailUrl" value="/product/getProduct">
    <c:param name="prodNo" value="${p.prodNo}" />
    <c:param name="menu" value="${param.menu}" />
    <c:param name="searchCondition" value="${search.searchCondition}" />
    <c:param name="searchKeyword" value="${search.searchKeyword}" />
    <c:param name="currentPage" value="${search.currentPage}" />
  </c:url>

  <%-- 2. 이제 tr과 a 태그에서는 미리 만들어둔 'detailUrl' 변수를 사용! --%>
  <tr class="product-item-table" data-href="${detailUrl}">
    <td>
      <c:set var="imgSrc" value="${cPath}/images/no-image.png" />
      <c:if test="${not empty p.imageFile}">
        <c:choose>
          <c:when test="${fn:startsWith(p.imageFile,'http://') or fn:startsWith(p.imageFile,'https://')}"><c:set var="imgSrc" value="${p.imageFile}" /></c:when>
          <c:when test="${fn:startsWith(p.imageFile,'/upload/product')}">
            <c:set var="imgSrc" value="${cPath}/images/uploadFiles/${fn:substringAfter(p.imageFile,'/upload/product/')}" />
          </c:when>
          <c:otherwise><c:set var="imgSrc" value="${cPath}/images/uploadFiles/${p.imageFile}" /></c:otherwise>
        </c:choose>
      </c:if>
      <img class="thumb" src="${imgSrc}" alt="thumb" onerror="this.onerror=null; this.src='${cPath}/images/no-image.png';" />
    </td>
    <td>
      <div class="prod-name"><a href="${detailUrl}"><c:out value="${p.prodName}" /></a></div>
      <div class="prod-detail"><c:out value="${p.prodDetail}" /></div>
    </td>
    <td align="center"><fmt:formatDate value="${p.regDate}" pattern="yyyy-MM-dd" /></td>
    <td align="center">
      <c:set var="pcode" value="${empty p.proTranCode ? '' : fn:trim(p.proTranCode)}" />
      <c:choose>
        <c:when test="${pcode == 'SOLD' || pcode == '판매완료'}">
          <span class="status-pill status-sold">판매완료</span>
          <c:if test="${isAdmin}">
            <span class="tran-toolbar" data-prodno="${p.prodNo}">
              <a href="#" data-code="1" class="tran-btn">1</a>
              <a href="#" data-code="2" class="tran-btn">2</a>
              <a href="#" data-code="3" class="tran-btn">3</a>
            </span>
          </c:if>
        </c:when>
        <c:otherwise>
          <span class="status-pill">판매중</span>
        </c:otherwise>
      </c:choose>
    </td>
    <td align="right" class="price-common" style="padding-right:12px;"><fmt:formatNumber value="${p.price}" type="number" /> 원</td>
  </tr>
      </c:forEach>
    </tbody>
  </table>

  <!-- 그리드(앨범) -->
  <div class="product-list-grid" style="display:none;">
    <c:forEach var="p" items="${list}">
      <div class="grid-card">
      
      <c:url var="detailUrlGrid" value="/product/getProduct">
	    <c:param name="prodNo" value="${p.prodNo}" />
	    <c:param name="menu" value="${param.menu}" />
	    <c:param name="searchCondition" value="${search.searchCondition}" />
	    <c:param name="searchKeyword" value="${search.searchKeyword}" />
	    <c:param name="currentPage" value="${search.currentPage}" />
	    <c:param name="view" value="${param.view}" /> 
	  </c:url>
		<a href="${detailUrlGrid}" class="product-item-grid" data-prodno="${p.prodNo}">
          <div class="thumb-wrap">
            <c:set var="imgSrc" value="${cPath}/images/no-image.png" />
            <c:if test="${not empty p.imageFile}">
              <c:choose>
                <c:when test="${fn:startsWith(p.imageFile,'http://') or fn:startsWith(p.imageFile,'https://')}"><c:set var="imgSrc" value="${p.imageFile}" /></c:when>
                <c:when test="${fn:startsWith(p.imageFile,'/upload/product')}">
                  <c:set var="imgSrc" value="${cPath}/images/uploadFiles/${fn:substringAfter(p.imageFile,'/upload/product/')}" />
                </c:when>
                <c:otherwise><c:set var="imgSrc" value="${cPath}/images/uploadFiles/${p.imageFile}" /></c:otherwise>
              </c:choose>
            </c:if>
            <img class="thumb" src="${imgSrc}" alt="thumb" onerror="this.onerror=null; this.src='${cPath}/images/no-image.png';" />
            <c:if test="${p.proTranCode == 'SOLD' || p.proTranCode == '판매완료'}">
              <span class="status-pill-grid">SOLD OUT</span>
            </c:if>
          </div>
          <div class="info">
            <div class="name"><c:out value="${p.prodName}" /></div>
            <div class="price price-common"><fmt:formatNumber value="${p.price}" type="number" /> 원</div>
          </div>
        </a>

        <c:if test="${isAdmin && (p.proTranCode == 'SOLD' || p.proTranCode == '판매완료')}">
          <div class="tran-toolbar tran-toolbar-grid" data-prodno="${p.prodNo}">
            <a href="#" data-code="1" class="tran-btn">1</a>
            <a href="#" data-code="2" class="tran-btn">2</a>
            <a href="#" data-code="3" class="tran-btn">3</a>
          </div>
        </c:if>
      </div>
    </c:forEach>
  </div>

  <c:if test="${empty list}">
    <div class="no-data-grid">등록된 상품이 없습니다.</div>
  </c:if>

  <div class="infinite-loading"></div>
  <div class="infinite-spacer"></div>

  <div id="product-popover" class="product-popover"></div>

</div>

<script>
$(function(){
  var currentView = '${param.view}';
  if (currentView === 'grid') {
    $('.product-list-table').hide();
    $('.product-list-grid').show();
  } else {
    $('.product-list-grid').hide();
    $('.product-list-table').show();
  }
});
</script>
</body>
</html>
