package spring.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import spring.domain.User;

/**
 * LogonCheckInterceptor
 * - 세션에 sessionUser가 없으면 new User()로 초기화
 * - sessionUser.isActive()로 로그인 여부 판단
 * - request attribute "loggedIn" 에 로그인 여부 전달
 * - 네비게이션(포워드/리다이렉트) 금지, 항상 컨트롤러까지 흐름 전달
 */
public class LogonCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getServletPath();
        System.out.println("[LogonCheckInterceptor] path=" + path);

        // 세션 확보 및 sessionUser 방어적 초기화
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("sessionUser");
        User sessionUser;

        if (obj instanceof User) {
            sessionUser = (User) obj;
        } else {
            sessionUser = new User(); // active=false 기본
            session.setAttribute("sessionUser", sessionUser);
            System.out.println("[LogonCheckInterceptor] sessionUser initialized");
        }

        boolean loggedIn = sessionUser.isActive();
        request.setAttribute("loggedIn", loggedIn);
        System.out.println("[LogonCheckInterceptor] loggedIn=" + loggedIn);

        // 네비게이션은 컨트롤러에서 처리
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 사용하지 않음
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // 사용하지 않음
    }
}
