<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/javascript/calendar.js"></script>

<html>
<head>
<meta charset="UTF-8">
<title><c:out value="상품 수정" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin.css" type="text/css">
		<script src="${cPath}/javascript/jquery-2.1.4.js"></script>
		<script src="${cPath}/javascript/product-ui.js"></script>
</head>

<body bgcolor="#ffffff" text="#000000">
	<div style="width: 98%; margin-left: 10px;">

		<c:choose>
			<c:when test="${not empty product}">

				<!-- 타이틀 -->
				<table width="100%" height="37" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td width="15" height="37"><img
							src="${pageContext.request.contextPath}/images/ct_ttl_img01.gif"
							width="15" height="37" /></td>
						<td
							background="${pageContext.request.contextPath}/images/ct_ttl_img02.gif"
							width="100%" style="padding-left: 10px;">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="93%" class="ct_ttl01"><c:out value="상품 수정" /></td>
								</tr>
							</table>
						</td>
						<td width="14" height="37"><img
							src="${pageContext.request.contextPath}/images/ct_ttl_img03.gif"
							width="14" height="37" /></td>
					</tr>
				</table>

				<!-- 상품 수정 form -->
				<form name="productForm"
					action="${pageContext.request.contextPath}/product/updateProduct"
					method="post" enctype="multipart/form-data">

					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="margin-top: 13px;">
						<tr>
							<td height="1" colspan="3" bgcolor="D6D6D6"></td>
						</tr>
						<tr>
							<td width="104" class="ct_write">상품명 <img
								src="${pageContext.request.contextPath}/images/ct_icon_red.gif"
								width="3" height="3" align="absmiddle" /></td>
							<td bgcolor="D6D6D6" width="1"></td>
							<td><input type="text" id="prodName" name="prodName"
								value="${product.prodName}" required /></td>
						</tr>

						<tr>
							<td height="1" colspan="3" bgcolor="D6D6D6"></td>
						</tr>
						<tr>
							<td width="104" class="ct_write">상품상세정보 <img
								src="${pageContext.request.contextPath}/images/ct_icon_red.gif"
								width="3" height="3" align="absmiddle" /></td>
							<td bgcolor="D6D6D6" width="1"></td>
							<td><textarea id="prodDetail" name="prodDetail" rows="4"
									cols="70">${product.prodDetail}</textarea></td>
						</tr>

						<tr>
							<td colspan="3" height="1" bgcolor="D6D6D6"></td>
						</tr>
						<tr>
							<td class="ct_write">제조일자 <img
								src="${pageContext.request.contextPath}/images/ct_icon_red.gif"
								width="3" height="3" align="absmiddle" /></td>
							<td bgcolor="D6D6D6"></td>
							<td class="ct_write01"><input type="text"
								name="manufactureDay" readonly="readonly" class="ct_input_g"
								style="width: 100px; height: 19px;" maxLength="10"
								value="${product.manufactureDay}" /> &nbsp; <img
								src="${pageContext.request.contextPath}/images/ct_icon_date.gif"
								width="15" height="15"
								onclick="show_calendar('document.productForm.manufactureDay', document.productForm.manufactureDay.value)">
							</td>
						</tr>

						<tr>
							<td height="1" colspan="3" bgcolor="D6D6D6"></td>
						</tr>
						<tr>
							<td width="104" class="ct_write">가격 <img
								src="${pageContext.request.contextPath}/images/ct_icon_red.gif"
								width="3" height="3" align="absmiddle" /></td>
							<td bgcolor="D6D6D6" width="1"></td>
							<td><input type="text" id="price" name="price"
								value="${product.price}" required /> 원</td>
						</tr>

						<tr>
							<td colspan="3" height="1" bgcolor="D6D6D6"></td>
						</tr>
						<tr>
							<td class="ct_write">상품이미지</td>
							<td bgcolor="D6D6D6"></td>
								<td class="ct_write01">
								  <c:if test="${not empty product.imageFile}">
								    <img src="${cPath}/upload/product/${product.imageFile}" style="max-width:120px;height:auto" />
								  </c:if>
							  <input type="file" name="fName"/>
							  <input type="hidden" name="prodNo" value="${product.prodNo}"></td>
						</tr>
					</table>

					<div style="text-align: right; margin-top: 10px;">
						<button type="submit">수정완료</button>
						<button type="button"
							onclick="location.href='${pageContext.request.contextPath}/product/getProduct?prodNo=${product.prodNo}'">취소</button>
					</div>

				</form>
			</c:when>
			<c:otherwise>
				<h3>상품 정보가 없습니다.</h3>
				<a href="${pageContext.request.contextPath}/product/listProduct">목록</a>
			</c:otherwise>
		</c:choose>

	</div>
	
	<script>
(function($){
  var $form = $("form[name='productForm']");
  if(!$form.length) return;

  var $price = $form.find("[name='price']");
  var $mday  = $form.find("[name='manufactureDay']");
  var $file  = $form.find("input[type='file']");

  // 가격 표시
  $price.on("input", function(){
    var raw = this.value.replace(/[^0-9]/g,"");
    this.value = raw ? Number(raw).toLocaleString() : "";
  });

  // 제조일 정리
  $mday.on("blur", function(){
    var v = (this.value||"").replace(/[^0-9]/g,"").substr(0,8);
    this.value = v;
  });

  // 파일 변경 미리보기
  $file.on("change", function(){
    var f = this.files && this.files[0];
    if(!f) return;
    $("#imageFileName, #fn_display").text(f.name);
    var url = URL.createObjectURL(f);
    $("#preview, #imagePreview").attr("src", url).show();
  });

  // 수정 경고(선택)
  var dirty=false;
  $form.find("input,textarea,select").on("change input", function(){ dirty=true; });
  $(window).on("beforeunload", function(){
    if(dirty) return "변경사항이 저장되지 않을 수 있어요.";
  });

  // 더블서밋 방지 + 전송 직전 정리
  var sending=false;
  $form.on("submit", function(){
    if(sending) return false;
    $price.val($price.val().replace(/[^0-9]/g,""));
    $mday.val(($mday.val()||"").replace(/[^0-9]/g,"").substr(0,8));
    sending=true; setTimeout(function(){ sending=false; },1500);
    // 페이지 떠날 때 경고 해제
    $(window).off("beforeunload");
  });
})(jQuery);
</script>
	
</body>
</html>
