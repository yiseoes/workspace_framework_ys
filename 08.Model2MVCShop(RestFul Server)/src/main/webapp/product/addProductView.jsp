<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/javascript/calendar.js"></script>

<html>
<head>
<meta charset="UTF-8">
<title><c:out value="상품 등록" /></title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin.css" type="text/css">
</head>

<body bgcolor="#ffffff" text="#000000">
	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm"
			action="${pageContext.request.contextPath}/product/listProduct"
			method="get">

			<!-- Title -->
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
								<td width="93%" class="ct_ttl01"><c:out value="상품 등록" /></td>
							</tr>
						</table>
					</td>
					<td width="14" height="37"><img
						src="${pageContext.request.contextPath}/images/ct_ttl_img03.gif"
						width="14" height="37" /></td>
				</tr>
			</table>
		</form>
		<!-- detailForm 종료 (타이틀만 감쌈) -->

		<!-- 실제 상품 등록 폼 시작 -->
		<form name="productForm"
			action="${pageContext.request.contextPath}/product/addProduct"
			method="post"  enctype="multipart/form-data">

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
					<td label for="prodName"><input type="text" id="prodName"
						name="prodName" required /></td>
				</tr>

				<tr>
					<td height="1" colspan="3" bgcolor="D6D6D6"></td>
				</tr>
				<tr>
					<td width="104" class="ct_write">상품상세정보 <img
						src="${pageContext.request.contextPath}/images/ct_icon_red.gif"
						width="3" height="3" align="absmiddle" /></td>
					<td bgcolor="D6D6D6" width="1"></td>
					<td label for="prodDetail"><textarea id="prodDetail"
							name="prodDetail" rows="4" cols="70"></textarea></td>
				</tr>

				<tr>
					<td colspan="3" height="1" bgcolor="D6D6D6"></td>
				</tr>
				<tr>
					<td class="ct_write">제조일자 <img
						src="${pageContext.request.contextPath}/images/ct_icon_red.gif"
						width="3" height="3" align="absmiddle" />
					</td>
					<td bgcolor="D6D6D6"></td>
					<td class="ct_write01"><input type="text"
						name="manufactureDay" readonly="readonly" class="ct_input_g"
						style="width: 100px; height: 19px;" maxLength="10"> &nbsp;
						<img
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
					<td label for="number"><input type="text" id="price" name="price" required
				       inputmode="numeric" pattern="[0-9]*"
				       oninput="this.value=this.value.replace(/[^0-9]/g,'');"> 원</td>
				</tr>

<!-- 				<tr> -->
<!-- 					<td colspan="3" height="1" bgcolor="D6D6D6"></td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="ct_write">상품이미지</td> -->
<!-- 					<td bgcolor="D6D6D6"></td> -->
<!-- 					<td class="ct_write01"> -->
<!-- 						원본 속성 표기 그대로 유지 <input type="text" name="fileName" -->
<!-- 						class="ct_input_g" width: "3" height: "3" maxLength="10"/ > -->
<!-- 					</td> -->
<!-- 				</tr> -->
				
				<tr>
					<td colspan="3" height="1" bgcolor="D6D6D6"></td>
				</tr>
				
				<tr>
					<td colspan="3" height="1" bgcolor="D6D6D6"></td>
				</tr>
				<tr>
				  <td class="ct_write">상품이미지</td>
				  <td bgcolor="D6D6D6"></td>
				  <td class="ct_write01">
				    <input type="file" id="imageFile" name="fName"
				           accept=".jpg,.jpeg,.png,.gif"
				           style="position:absolute; left:-9999px; width:1px; height:1px; opacity:0;"
				           onchange="document.getElementById('imageFileName').textContent = (this.files && this.files[0] ? this.files[0].name : '선택된 파일 없음');" />
				
				    <!-- ✅ 기본 회색 버튼 스타일 -->
				    <label for="imageFile"
				           style="
				             display:inline-block; cursor:pointer;
				             padding:2px 10px; line-height:22px;
				             background:#eee; color:#000;
				             border:1px solid #888; border-radius:3px;
				             box-shadow: inset 0 1px 0 #fff;
				             background-image:none !important;  /* admin.css 배경 이미지 무시 */
				           "
				           onmouseover="this.style.background='#e2e2e2'"
				           onmouseout="this.style.background='#eee'"
				           onmousedown="this.style.background='#ddd'"
				           onmouseup="this.style.background='#e2e2e2'">
				      이미지 선택
				    </label>
				
				    <span id="imageFileName" class="ct_input_g" style="display:inline-block; min-width:220px;">
				      선택된 파일 없음
				    </span>
				  </td>
				</tr>


			</table>

			<div style="text-align: right; margin-top: 10px;">
				<button type="submit">등록</button>
				<button type="reset"
					onclick="location.href='${pageContext.request.contextPath}/product/listProduct?prodNo=${product.prodNo}'">취소</button>
			</div>
				
		</form>
		<!-- productForm 종료 -->

	</div>
</body>
</html>
