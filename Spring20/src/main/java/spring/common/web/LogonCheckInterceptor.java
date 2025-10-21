package spring.common.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import spring.domain.User;

public class LogonCheckInterceptor implements HandlerInterceptor {

    private static final String SESSION_KEY = "sessionUser";

    private boolean isLoggedIn(HttpSession session) {
        if (session == null) return false;
        Object obj = session.getAttribute(SESSION_KEY);
        if (!(obj instanceof User)) return false;
        return ((User) obj).isActive();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        final String path = request.getServletPath();
        final HttpSession session = request.getSession(false);
        final boolean loggedIn = isLoggedIn(session);

        System.out.println("\n[LogonCheckInterceptor] 요청 URL : " + path);
        System.out.println("[LogonCheckInterceptor] 로그인 상태 : " + (loggedIn ? "로그인됨" : "미로그인"));

        switch (path) {
            case "/logon":
                if (loggedIn) {
                    System.out.println("→ 로그인 상태에서 logon.do 접근 → home.jsp로 forward");
                    forward(request, response, "/user002/home.jsp");
                    return false;
                } else {
                    System.out.println("→ 미로그인 상태에서 logon.do 접근 → 컨트롤러로 진행");
                    return true;
                }

            case "/home":
                if (!loggedIn) {
                    System.out.println("→ 미로그인 상태에서 home.do 접근 → logon.jsp로 forward");
                    forward(request, response, "/user002/logon.jsp");
                    return false;
                } else {
                    System.out.println("→ 로그인 상태에서 home.do 접근 → 컨트롤러로 진행");
                    return true;
                }

            case "/logonAction":
                if (loggedIn) {
                    System.out.println("→ 로그인 상태에서 logonAction.do 접근 → home.jsp로 forward");
                    forward(request, response, "/user002/home.jsp");
                    return false;
                } else {
                    System.out.println("→ 미로그인 상태에서 logonAction.do 접근 → 컨트롤러로 진행");
                    return true;
                }

            case "/logout":
                if (!loggedIn) {
                    System.out.println("→ 미로그인 상태에서 logout.do 접근 → logon.jsp로 forward");
                    forward(request, response, "/user002/logon.jsp");
                    return false;
                } else {
                    System.out.println("→ 로그인 상태에서 logout.do 접근 → 컨트롤러로 진행");
                    return true;
                }

            default:
                System.out.println("→ 대상이 아닌 URL, 그대로 진행");
                return true;
        }
    }

    private void forward(HttpServletRequest request,
                         HttpServletResponse response,
                         String viewPath) throws Exception {
        RequestDispatcher rd = request.getRequestDispatcher(viewPath);
        rd.forward(request, response);
    }
}
