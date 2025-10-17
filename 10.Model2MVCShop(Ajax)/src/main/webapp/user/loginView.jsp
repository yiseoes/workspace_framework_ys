<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <base target="_top">
  <title>로그인</title>

  <script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
  <link href="/css/style.css" rel="stylesheet" type="text/css">

  <!-- 공용 유효성 + alert 라우팅 + 메시지 한글화 -->
  <script src="/javascript/CommonScript.js?v=login-hook-2"></script>
  <!-- 로그인 화면 전용 바인딩/AJAX -->
  <script src="/javascript/loginView.js?v=1"></script>
</head>
<body class="login-body">
  <div class="login-container">
    <div class="login-header"><h1>LOGIN</h1><p>🍭</p></div>

    <!-- 에러 메시지 표시 영역 -->
    <div id="formError" style="color:#d33; font-size:13px; min-height:18px; margin:6px 0 10px;"></div>

    <form id="loginForm" onsubmit="return false;" novalidate>
      <input type="text" id="userId" name="userId" placeholder="아이디"
             required fieldTitle="아이디" minLength="4" autocomplete="username">
      <input type="password" id="password" name="password" placeholder="비밀번호"
             required fieldTitle="비밀번호" minLength="4" autocomplete="current-password">
      <button type="submit" id="btnLogin">로그인</button>
    </form>

    <div class="login-links">
      <a href="#">아이디 찾기</a> |
      <a href="#">비밀번호 찾기</a> |
      <a href="/user/addUser" id="linkAddUser">회원가입</a>
    </div>

    <div class="social-divider">OR</div>

    <div class="social-login">
      <!-- href는 JS가 동적으로 채움 -->
      <a id="btnKakao"  class="social-btn kakao"  href="#">카카오 계정으로 로그인</a>
      <a id="btnGoogle" class="social-btn google" href="#">구글 계정으로 로그인</a>
      <a id="btnNaver"  class="social-btn naver"  href="#" target="_top">네이버 계정으로 로그인</a>
    </div>
  </div>

  <script type="text/javascript">
    (function () {
      // 현재 실행 중인 호스트/포트 (http/https + hostname + :port)
      var origin = window.location.protocol + '//' + window.location.hostname + (window.location.port ? ':' + window.location.port : '');

      // 컨텍스트패스 추정: <base> 또는 현재 경로 기준.
      // /user/loginView.jsp 같은 경로일 때 컨텍스트패스가 루트라면 빈 문자열이 될 수 있음.
      function guessContextPath() {
        // JSP에서 서버가 cPath를 주지 않으니, URL pathname으로 추정
        // 예: /Spring13/user/loginView.jsp -> /Spring13
        var p = window.location.pathname || '';
        if (!p) return '';
        // 두 번째 슬래시 위치까지 잘라 컨텍스트로 취급
        // 1) /user/loginView.jsp -> '' (루트 컨텍스트)
        // 2) /Spring13/user/loginView.jsp -> /Spring13
        var parts = p.split('/');
        // parts: ["", "Spring13", "user", "loginView.jsp"] or ["", "user", "loginView.jsp"]
        if (parts.length >= 3) {
          // 맨 앞은 빈 문자열, 그 다음 토큰이 컨텍스트 후보
          // 컨텍스트 뒤에 바로 user가 온다면 컨텍스트 있음
          return (parts[1] && parts[1] !== 'user') ? ('/' + parts[1]) : '';
        }
        return '';
      }

      var cPath = guessContextPath();

      // 각 서비스 콜백(절대경로)
      var redirectKakao  = origin + cPath + '/user/kakao/callback';
      var redirectGoogle = origin + cPath + '/user/google/callback';
      var naverLoginUrl  = origin + cPath + '/user/naver/login';

      // ===== Kakao Authorize URL (redirect_uri 동적 주입) =====
      var kakaoAuth =
        'https://kauth.kakao.com/oauth/authorize'
        + '?client_id=' + encodeURIComponent('cf296aed85af05aab502785a1c9d6497')
        + '&redirect_uri=' + encodeURIComponent(redirectKakao)
        + '&response_type=code'
        + '&scope=' + encodeURIComponent('profile_nickname account_email')
        + '&prompt=login';

      // ===== Google Authorize URL (redirect_uri 동적 주입) =====
      var googleAuth =
        'https://accounts.google.com/o/oauth2/v2/auth'
        + '?client_id=' + encodeURIComponent('516891552299-0dhkpa2dm2cls435lv832farshtb5m0h.apps.googleusercontent.com')
        + '&redirect_uri=' + encodeURIComponent(redirectGoogle)
        + '&response_type=code'
        + '&scope=' + encodeURIComponent('https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile')
        + '&prompt=select_account'
        + '&access_type=offline'
        + '&include_granted_scopes=true';

      // 앵커에 href 주입
      var btnK = document.getElementById('btnKakao');
      var btnG = document.getElementById('btnGoogle');
      var btnN = document.getElementById('btnNaver');

      if (btnK) btnK.setAttribute('href', kakaoAuth);
      if (btnG) btnG.setAttribute('href', googleAuth);
      if (btnN) btnN.setAttribute('href', naverLoginUrl);

      // 디버깅 로그(필요 시 주석 풀고 확인)
      console.log('[SOCIAL][DBG] origin=', origin, ' cPath=', cPath);
      console.log('[SOCIAL][DBG] KAKAO redirect_uri=', redirectKakao);
      console.log('[SOCIAL][DBG] GOOGLE redirect_uri=', redirectGoogle);
      console.log('[SOCIAL][DBG] NAVER start url=', naverLoginUrl);
    })();
  </script>
</body>
</html>
