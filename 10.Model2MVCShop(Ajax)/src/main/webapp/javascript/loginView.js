// /javascript/loginView.js
(function ($) {

  // AJAX 로그인
  function runLoginAjax(id, pw) {
    $("#formError").text("");

    $.ajax({
      url: "/user/json/login",
      method: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify({ userId: id, password: pw })
    }).done(function (data) {
      // 서버가 돌려준 userId/password가 내가 보낸 값과 일치할 때만 성공으로 간주
      var ok = !!(data && data.userId && data.password &&
                  data.userId === id && data.password === pw);

      if (ok) {
        alert((data.userName ? data.userName : "회원") + "님, 환영합니다!");
        if (window.parent) { window.parent.location.href = "/index.jsp"; }
        else { window.location.href = "/index.jsp"; }
      } else {
        // ✅ 아이디/비밀번호 불일치 안내
        $("#formError").text("아이디와 비밀번호를 다시 확인해 주세요.");
        $("#password").val("").focus();
      }
    }).fail(function () {
      $("#formError").text("로그인 중 오류가 발생했습니다. 다시 시도해 주세요.");
      $("#password").val("").focus();
    });
  }

  $(function () {
    // 입력 중 공백 제거 + 에러 문구 초기화
    $("#userId, #password").on("input", function () {
      var v = $(this).val();
      if (v !== v.trim()) $(this).val(v.trim());
      $("#formError").text("");
    });

    // 제출
    $("#loginForm").on("submit", function (e) {
      e.preventDefault();

      var id = ($("#userId").val() || "").trim();
      var pw = ($("#password").val() || "").trim();

      // ✅ 공백 케이스별 메시지
      if (!id && !pw) {
        $("#formError").text("아이디와 비밀번호를 입력해 주세요.");
        $("#userId").focus();
        return;
      }
      if (!id) {
        $("#formError").text("아이디를 입력해 주세요.");
        $("#userId").focus();
        return;
      }
      if (!pw) {
        $("#formError").text("비밀번호를 입력해 주세요.");
        $("#password").focus();
        return;
      }

      // 다른 제약(길이/포맷 등)은 기존 CommonScript로 검증
      if (!FormValidation(this)) return;

      runLoginAjax(id, pw);
    });

    // 회원가입 이동
    $("#linkAddUser").on("click", function (e) {
      e.preventDefault();
      if (window.parent) { window.parent.location.href = "/user/addUser"; }
      else { window.location.href = "/user/addUser"; }
    });
  });

})(jQuery);
