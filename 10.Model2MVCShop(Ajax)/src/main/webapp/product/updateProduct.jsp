<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:set var="cPath" value="${pageContext.request.contextPath}" />
    <title>상품 수정</title>
    
    <link rel="stylesheet" href="${cPath}/css/admin.css" type="text/css">
    <%-- 버전 숫자만 1에서 2로 수정! --%>
    <link rel="stylesheet" href="${cPath}/css/product-form.css?v=2" type="text/css">
    
    <script type="text/javascript" src="${cPath}/javascript/calendar.js"></script>
</head>
<body>
    <div class="prod-wrap">
        <c:choose>
            <c:when test="${not empty product}">
                <div class="page-title">상품 수정</div>

                <form name="productForm" action="${cPath}/product/updateProduct" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="prodNo" value="${product.prodNo}" />
                    <input type="hidden" name="imageFile" value="${product.imageFile}" />
                    
                        <input type="hidden" name="searchCondition" value="${param.searchCondition}" />
					    <input type="hidden" name="searchKeyword" value="${param.searchKeyword}" />
					    <input type="hidden" name="currentPage" value="${param.currentPage}" />
					    <input type="hidden" name="view" value="${param.view}" />
                
                    <div class="form-container">
						<div class="form-row">
						    <div class="label-col">상품명 <span class="required-mark">*</span></div>
						    <div class="input-col">
						        <input type="text" id="prodName" name="prodName" value="<c:out value='${product.prodName}'/>" required />
						    </div>
						</div>
						
						<div class="form-row">
						    <div class="label-col">상품 상세 정보 <span class="required-mark">*</span></div>
						    <div class="input-col">
						        <textarea id="prodDetail" name="prodDetail" required><c:out value='${product.prodDetail}'/></textarea>
						    </div>
						</div>

                        <div class="form-row">
                            <div class="label-col">제조일자 <span class="required-mark">*</span></div>
                            <div class="input-col">
                                <div class="input-with-unit">
                                    <input type="text" id="manufactureDay" name="manufactureDay" readonly="readonly" style="width:150px;" maxLength="10" placeholder="YYYY-MM-DD" value="${product.manufactureDay}" required>
                                    <img src="${cPath}/images/ct_icon_date.gif" width="15" height="15" class="calendar-icon"
                                         onclick="show_calendar('document.productForm.manufactureDay', document.productForm.manufactureDay.value)">
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="label-col">가격 <span class="required-mark">*</span></div>
                            <div class="input-col">
                                <div class="input-with-unit">
                                    <input type="text" id="price" name="price" value="<fmt:formatNumber value="${product.price}" groupingUsed="true" />" required inputmode="numeric" pattern="[0-9,]*">
                                    <span>원</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="label-col">상품 이미지</div>
                            <div class="input-col file-upload-group">
                                <input type="file" id="imageFile" name="fName" accept=".jpg,.jpeg,.png,.gif,.jfif" style="display:none;">
                                <label for="imageFile" class="btn-file-select">이미지 변경</label>
                                <span id="imageFileName" class="file-name">
                                    <c:out value="${empty product.imageFile ? '선택된 파일 없음' : product.imageFile}" />
                                </span>
                                
                                <c:if test="${not empty product.imageFile}">
                                  <img id="imagePreview" src="${cPath}/images/uploadFiles/${product.imageFile}" alt="미리보기" class="image-preview" />
                                </c:if>
                                <c:if test="${empty product.imageFile}">
                                  <img id="imagePreview" alt="미리보기" style="display:none;" class="image-preview" />
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn-primary">수정 완료</button>
                        <button type="button" class="btn-secondary" onclick="location.href='${cPath}/product/getProduct?prodNo=${product.prodNo}'">취소</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="page-title">오류</div>
                <p>상품 정보를 찾을 수 없습니다.</p>
                <a href="${cPath}/product/listProduct" class="btn-secondary">목록으로 돌아가기</a>
            </c:otherwise>
        </c:choose>
    </div>

    <script>
    (function(){
      // 파일 업로드 UI 처리
      var fileInput = document.getElementById('imageFile');
      if(fileInput) {
        fileInput.addEventListener('change', function(e) {
            var file = this.files && this.files[0];
            var fileNameSpan = document.getElementById('imageFileName');
            var imagePreview = document.getElementById('imagePreview');

            if (file) {
                fileNameSpan.textContent = file.name;
                var reader = new FileReader();
                reader.onload = function(event) {
                    imagePreview.src = event.target.result;
                    imagePreview.style.display = 'block';
                };
                reader.readDataURL(file);
            }
        });
      }
      
      // 폼 제출 관련 스크립트
      var form = document.forms['productForm'];
      if(!form) return;

      var price = form.querySelector("[name='price']");
      if(price){
        price.addEventListener("input", function(){
          var raw = this.value.replace(/[^0-9]/g,"");
          this.value = raw ? Number(raw).toLocaleString() : "";
        });
      }

      var sending = false;
      form.addEventListener("submit", function(e){
        if(sending){ e.preventDefault(); return false; }
        if(price){ price.value = price.value.replace(/[^0-9]/g,""); }
        sending = true;
        setTimeout(function(){ sending=false; }, 1500);
      });
    })();
    </script>
</body>
</html>