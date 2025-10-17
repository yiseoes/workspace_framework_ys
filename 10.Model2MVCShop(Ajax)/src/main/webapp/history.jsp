<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>열어본 상품 보기</title>

<!-- 캐시 비활성화 -->
<meta http-equiv="Cache-Control"
	content="no-store, no-cache, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<!-- 컨텍스트 경로 -->
<c:set var="cPath" value="${pageContext.request.contextPath}" />

<style>

/* 파스텔 톤 컬러 팔레트 */
body {
	margin: 0;
	background: radial-gradient(1200px 600px at 10% -10%, var(--bg-grad-a),
		transparent 50%),
		radial-gradient(1000px 700px at 110% 0%, var(--bg-grad-b), transparent
		55%), linear-gradient(180deg, #ffffff, #ffffff);
	color: var(--ink-1);
	font-family: ui-rounded, "Apple SD Gothic Neo", "Segoe UI", Roboto,
		"Noto Sans KR", system-ui, -apple-system, sans-serif;
}

/* 컨테이너 */
.his-wrap {
	max-width: 1100px;
	margin: 32px auto 40px;
	padding: 0 20px;
}

.his-title {
	font-size: 15px;
	font-weight: 800;
	letter-spacing: .2px;
	margin: 8px 0 18px;
	color: var(--ink-1);
	position: relative;
	display: inline-block;
}

/* 빈 상태 */
.empty {
	color: var(--muted);
	background: #fff;
	border: 2px dashed #f3eaff;
	padding: 22px 18px;
	border-radius: var(--radius);
	text-align: center;
	box-shadow: var(--shadow);
}

/* 그리드 */
ul.his {
	list-style: none;
	padding: 0;
	margin: 0;
	display: grid;
	grid-gap: 18px;
	grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
}

/* 카드 */
.his-item {
	background: var(--card);
	border: 1.5px solid var(--stroke);
	border-radius: var(--radius);
	box-shadow: var(--shadow);
	transition: transform .18s ease, box-shadow .18s ease, border-color .18s
		ease;
	will-change: transform;
}

.his-item:hover {
	transform: translateY(-2px) scale(1.01);
	box-shadow: var(--shadow-hover);
	border-color: #eadcff;
}

.his-item:active {
	transform: translateY(0) scale(.995);
}

/* 링크(블록 전체 클릭) */
.his-link {
	display: block;
	padding: 16px 18px;
	text-decoration: none;
	color: inherit;
}

/* 상단 행: 아이콘 + 이름 */
.his-top {
	display: flex;
	align-items: center;
	gap: 12px;
	margin-bottom: 8px;
}

/* 젤리곰 아이콘 */
.his-gummy {
	width: 26px;
	height: 32px;
	flex: 0 0 26px;
	display: inline-block;
	vertical-align: middle;
	color: var(--gummy, #ff7aa2); /* JS가 이 color를 카드별로 랜덤 세팅 */
	filter: drop-shadow(0 2px 0 rgba(0, 0, 0, .04))
		drop-shadow(0 8px 14px rgba(124, 58, 237, .18));
}

.his-gummy svg {
	width: 100%;
	height: 100%;
	display: block;
}

/* 살짝 젤리 느낌(빛반사) */
.his-gummy .gloss {
	fill: #fff;
	opacity: .22;
}

/* 호버 시 더 말랑말랑 */
.his-item:hover .his-gummy {
	transform: translateY(-1px) scale(1.03);
	transition: transform .18s ease;
}

/* 상품명(말줄임) */
.his-name {
	font-size: 12.5px;
	font-weight: 700;
	color: var(--ink-1);
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

/* 보조 라인 */
.his-sub {
	font-size: 10.5px;
	color: var(--ink-2);
	opacity: .8;
	margin-top: 6px;
	display: flex;
	gap: 10px;
	align-items: center;
}

.his-sub .sep {
	color: #d8d8d8;
}

/* 카드에 반짝 포인트 */
.his-item::after {
	content: "";
	position: absolute;
	pointer-events: none;
}

.his-item:hover .his-name {
	text-shadow: 0 1px 0 #fff, 0 0 18px var(--glow);
}

/* 포커스 접근성 */
.his-link:focus {
	outline: 3px solid rgba(124, 58, 237, .35);
	outline-offset: 3px;
	border-radius: calc(var(--radius)- 2px);
}

/* 작은 화면 튜닝 */
@media ( max-width :600px) {
	.his-wrap {
		margin: 20px auto 28px;
	}
	.his-title {
		font-size: 15px;
	}
	.his-link {
		padding: 14px 16px;
	}
	.his-eye {
		width: 22px;
		height: 22px;
		font-size: 11px;
	}
}

/* 모션 최소화 요청 존중 */
@media ( prefers-reduced-motion : reduce) {
	.his-item, .his-item:hover {
		transition: none;
		transform: none;
	}
	.his-name {
		text-shadow: none !important;
	}
}
</style>

</head>

<body>

	<!-- 1) 계정별 쿠키명 결정 : history_<userId> 또는 history -->
	<c:set var="uid" value="" />
	<c:choose>
		<c:when
			test="${not empty sessionScope.user and not empty sessionScope.user.userId}">
			<c:set var="uid" value="${fn:trim(sessionScope.user.userId)}" />
		</c:when>
		<c:when
			test="${not empty sessionScope.userVO and not empty sessionScope.userVO.userId}">
			<c:set var="uid" value="${fn:trim(sessionScope.userVO.userId)}" />
		</c:when>
	</c:choose>
	<c:choose>
		<c:when test="${empty uid}">
			<c:set var="cookieName" value="history" />
		</c:when>
		<c:otherwise>
			<c:set var="cookieName" value="history_${uid}" />
		</c:otherwise>
	</c:choose>

	<!-- 2) 쿠키 안전 조회(Map 순회로 동적 키 매칭) -->
	<c:set var="historyStr" value="" />
	<c:forEach var="entry" items="${cookie}">
		<!-- entry.key = 쿠키명, entry.value = Cookie -->
		<c:if test="${entry.key eq cookieName}">
			<c:set var="historyStr" value="${entry.value.value}" />
		</c:if>
	</c:forEach>

	<!-- 3) 토큰 분리(콤마/콜론 호환) + 최신→과거 + 중복 제거 -->
	<c:choose>
		<c:when test="${not empty historyStr}">
			<c:set var="histArr" value="${fn:split(historyStr, '[,:]')}" />
			<c:set var="len" value="${fn:length(histArr)}" />

			<div class="his-wrap">
				<div class="his-title">💖당신이 열어본 상품을 알고 있다💖</div>
				<ul class="his">
					<c:forEach var="i" begin="0" end="${len-1}">
						<c:set var="rev" value="${len - 1 - i}" />
						<c:set var="v" value="${fn:trim(histArr[rev])}" />
						<c:if test="${not empty v and v ne 'null'}">
							<li class="his-item"><a class="his-link"
								href="${cPath}/product/getProduct?prodNo=${v}&menu=search"
								target="rightFrame">
									<div class="his-top">
										<span class="his-gummy" aria-hidden="true"> <svg
												viewBox="0 0 64 80" xmlns="http://www.w3.org/2000/svg">
		                <g fill="currentColor">
		                  <circle cx="18" cy="12" r="8" />
												<circle cx="46" cy="12" r="8" />
		                  <circle cx="32" cy="26" r="16" />
		                  <rect x="14" y="38" width="36" height="32" rx="16"
													ry="16" />
		                  <circle cx="22" cy="72" r="8" />
												<circle cx="42" cy="72" r="8" />
		                </g>
		                <ellipse class="gloss" cx="26" cy="20" rx="8" ry="6" />
		              </svg>
										</span> <span class="his-name">[${v}] 상품 보기</span>
									</div>
									<div class="his-sub">
										<span>클릭하여 상세로 이동</span>
									</div>
							</a></li>
						</c:if>
					</c:forEach>
				</ul>

			</div>
		</c:when>

		<c:otherwise>
			<div class="his-wrap">
				<div class="his-title">💖당신이 열어본 상품을 알고 있다💖</div>
				<div class="empty">최근 열어본 상품이 없습니다.</div>
			</div>
		</c:otherwise>
	</c:choose>
	<script>
		(function() {
			var gummies = document.querySelectorAll('.his-gummy');
			for (var i = 0; i < gummies.length; i++) {
				// 파스텔 계열 HSL 랜덤 (채도/명도 높게)
				var h = Math.floor(Math.random() * 360); // 0~359
				var s = 75 + Math.floor(Math.random() * 15); // 75~90%
				var l = 62 + Math.floor(Math.random() * 10); // 62~71%
				gummies[i].style.color = 'hsl(' + h + ',' + s + '%,' + l + '%)';
			}
		})();

		//상세 링크 클릭 시, 항상 캐시 방지 파라미터(ts) 추가
		document.addEventListener('click', function(e) {
			var a = e.target.closest('.his-link');
			if (!a)
				return;
			e.preventDefault();

			// 현재 href 기반으로 ts 파라미터 추가
			var base = a.getAttribute('href');
			// 상대경로 안전 처리
			var url = new URL(base, window.location.origin);
			url.searchParams.set('ts', Date.now().toString());

			// target 보존(예: rightFrame)
			var tgt = a.getAttribute('target') || '_self';
			window.open(url.toString(), tgt);
		});
	</script>

</body>
</html>
