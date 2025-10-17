package com.model2.mvc.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model2.mvc.service.domain.User;

/*
 * FileName : LogonCheckInterceptor.java 
 *  - Controller 호출 전/후 처리
 *  - 로그인/권한/공개자원 허용 + 상품열람 히스토리 기록
 *  - 08/10 혼용 URL 패턴 동시 지원(점진적 리팩토링 용)
 */
public class LogonCheckInterceptor extends HandlerInterceptorAdapter {

    /// Constructor
    public LogonCheckInterceptor() {
        System.out.println("\nCommon :: " + this.getClass() + "\n");
    }

    /// Method
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println("\n[ LogonCheckInterceptor start........]");
        final String method = request.getMethod();
        final String uri = request.getRequestURI();

        // 컨텍스트 제거한 순수 path 산출 (정적/공개 판정은 반드시 path 기준)
        final String ctx  = request.getContextPath();   // 예) /Model2MVCShop
        final String path = uri.substring(ctx.length()); // 예) /product/getProduct

        // 세션 사용자 : 08(userVO) / 10(user) 혼재 흡수
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            Object alt = session.getAttribute("userVO");
            if (alt instanceof User) {
                user = (User) alt;
            }
        }

        // =====================[최근 본 상품 히스토리 기록]=====================
        // - GET /product/getProduct(08) 또는 /getProduct.do(10) 접근 시 누적
        try {
            boolean isGet = "GET".equalsIgnoreCase(method);

            boolean isDetail08 = (path != null) && path.contains("/product/getProduct");
            boolean isDetail10 = (path != null) && path.contains("/getProduct.do");
            boolean isDetail = isGet && (isDetail08 || isDetail10);

            if (isDetail) {
                String prodNoStr = request.getParameter("prodNo");
                if (prodNoStr != null && prodNoStr.length() > 0) {
                    String uid = (user != null && user.getUserId() != null) ? user.getUserId().trim() : "";
                    String cookieName = (uid.length() > 0) ? ("history_" + uid) : "history";

                    // 가장 긴 기존 값 선택
                    String history = null;
                    javax.servlet.http.Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        int maxLen = -1;
                        for (javax.servlet.http.Cookie c : cookies) {
                            if (cookieName.equals(c.getName())) {
                                String v = c.getValue();
                                int len = (v == null ? 0 : v.length());
                                if (len > maxLen) {
                                    maxLen = len;
                                    history = v;
                                }
                            }
                        }
                    }

                    final String DELIM = ":"; // RFC 허용 문자 사용
                    String newHistory = (history == null || history.length() == 0) ? prodNoStr : history + DELIM + prodNoStr;

                    // 최대 100개 유지(앞에서부터 절단)
                    String[] parts = newHistory.split(java.util.regex.Pattern.quote(DELIM));
                    if (parts.length > 100) {
                        int start = parts.length - 100;
                        StringBuilder sb = new StringBuilder();
                        for (int i = start; i < parts.length; i++) {
                            if (sb.length() > 0) sb.append(DELIM);
                            sb.append(parts[i]);
                        }
                        newHistory = sb.toString();
                    }

                    javax.servlet.http.Cookie hc = new javax.servlet.http.Cookie(cookieName, newHistory);
                    hc.setPath("/");
                    hc.setMaxAge(60 * 60 * 24 * 7); // 7일
                    response.addCookie(hc);
                }
            }
        } catch (Exception e) {
            System.out.println("[HISTORY][WARN] 인터셉터 기록 중 예외 : " + e);
            // 히스토리 실패는 흐름 차단하지 않음
        }
        // ==================================================================

        // ---------- 로그인 상태 ----------
        if (user != null) {

            // 로그인 상태에서 불필요한 URI 차단(공통)
            if (containsAny(path, "addUser", "login", "checkDuplication")) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                System.out.println("[ 로그인 상태 불필요 요청 차단 ] uri=" + uri);
                System.out.println("[ LogonCheckInterceptor end........]\n");
                return false;
            }

            // 관리자 전용 자원 제어 : 08/10 패턴 동시 대응
            boolean adminArea =
                    // 10 스타일(.do)
                    containsAny(path, "addProductView.do", "addProduct.do", "updateProductView.do", "updateProduct.do")
                    // 08 스타일(/product/xxx)
                    || containsAny(path, "/product/addProductView", "/product/addProduct", "/product/updateProductView", "/product/updateProduct");

            if (adminArea) {
                boolean isAdmin =
                           "admin".equalsIgnoreCase(nvl(user.getUserId()))
                        || "admin".equalsIgnoreCase(nvl(user.getRole()))
                        || "role_admin".equalsIgnoreCase(nvl(user.getRole()))
                        || "a".equalsIgnoreCase(nvl(user.getRole()));

                if (!isAdmin) {
                    request.setAttribute("message", "관리자만 접근 가능합니다.");
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    System.out.println("[ 차단 ] 비관리자 관리자영역 접근 : uri=" + uri + " , userId=" + nvl(user.getUserId()) + " , role=" + nvl(user.getRole()));
                    System.out.println("[ LogonCheckInterceptor end........]\n");
                    return false;
                }
            }

            System.out.println("[ 로그인 상태 허용 ] uri=" + uri);
            System.out.println("[ LogonCheckInterceptor end........]\n");
            return true;
        }

        // ---------- 비로그인 상태 ----------
        // 1) 로그인/회원가입/중복체크는 허용
        if (containsAny(path, "addUser", "login", "checkDuplication")) {
            System.out.println("[ 비로그인 : 인증 관련 허용 ] uri=" + uri);
            System.out.println("[ LogonCheckInterceptor end........]\n");
            return true;
        }

        // 2) 공개 페이지(비로그인 허용) 화이트리스트 : 08/10 동시 지원
        boolean isPublic =
                   // 목록/검색
                   containsAny(path,
                       "/product/listProduct",   // 08 : /product/listProduct?menu=...
                       "/listProduct",           // 10 : /listProduct.do?menu=...
                       "/listProduct.do"
                   )
                || // 상세
                   containsAny(path,
                       "/product/getProduct",    // 08
                       "/getProduct",            // 10
                       "/getProduct.do"
                   )
                || // 정적/공통 (반드시 path 기준)
                   path.equals("/index.jsp")
                || path.contains("/history.jsp")
                || path.startsWith("/css/")
                || path.startsWith("/javascript/")
                || path.startsWith("/images/")
                || path.startsWith("/upload/")   // 내부 저장소 정적 서빙 시
                // 소셜로그인 엔드포인트 존재 시(없으면 무시)
                || containsAny(path, "/user/kakao", "/user/google", "/user/naver");

        if (isPublic) {
            System.out.println("[ 비로그인 공개 리소스 허용 ] uri=" + uri);
            System.out.println("[ LogonCheckInterceptor end........]\n");
            return true;
        }

        // 3) 그 외는 로그인 필요 : 프레임 중첩 방지 위해 redirect 권장
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        System.out.println("[ 비로그인 차단 → index로 유도 ] uri=" + uri);
        System.out.println("[ LogonCheckInterceptor end........]\n");
        return false;
    }

    // 편의 메서드
    private static String nvl(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean containsAny(String text, String... keys) {
        if (text == null || keys == null) return false;
        for (String k : keys) {
            if (k != null && text.contains(k)) return true;
        }
        return false;
    }
}
