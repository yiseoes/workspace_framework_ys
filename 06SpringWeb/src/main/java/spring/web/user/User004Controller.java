package spring.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import spring.domain.User;
import spring.service.user.impl.UserDAO;

/**
 * User004Controller
 * - 인터셉터가 sessionUser 보장 및 loggedIn 플래그 전달
 * - 컨트롤러는 화면 네비게이션만 담당
 * - JSP 경로 : /user002/logon.jsp, /user002/home.jsp
 */
@Controller
public class User004Controller {

    public User004Controller() {
        System.out.println("==> User004Controller default Constructor call.............");
    }

    // 로그인 화면 / 로그인 시 홈
    @RequestMapping("/logon.do")
    public ModelAndView logon(@RequestAttribute(name = "loggedIn", required = false) Boolean loggedIn,
                              HttpSession session) {

        System.out.println("[User004Controller::logon()] start");

        // 인터셉터가 보장하지만 방어적으로 처리
        if (loggedIn == null) {
            Object su = session.getAttribute("sessionUser");
            loggedIn = (su instanceof User) && ((User) su).isActive();
        }

        String viewName = loggedIn ? "/user002/home.jsp" : "/user002/logon.jsp";
        String message  = loggedIn ? "[logon()] Welcome" : "[logon()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName(viewName);
        mv.addObject("message", message);

        System.out.println("[logon] loggedIn=" + loggedIn + ", view=" + viewName);
        System.out.println("[User004Controller::logon()] end\n");
        return mv;
    }

    // 홈 / 미로그인 시 로그인 페이지
    @RequestMapping("/home.do")
    public ModelAndView home(@RequestAttribute(name = "loggedIn", required = false) Boolean loggedIn,
                             HttpSession session) {

        System.out.println("[User004Controller::home()] start");

        if (loggedIn == null) {
            Object su = session.getAttribute("sessionUser");
            loggedIn = (su instanceof User) && ((User) su).isActive();
        }

        String viewName = loggedIn ? "/user002/home.jsp" : "/user002/logon.jsp";
        String message  = loggedIn ? "[home()] Welcome" : "[home()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName(viewName);
        mv.addObject("message", message);

        System.out.println("[home] loggedIn=" + loggedIn + ", view=" + viewName);
        System.out.println("[User004Controller::home()] end\n");
        return mv;
    }

    // logonAction.do GET : 폼 없이 접근할 때 로그인 페이지로 유도
    @RequestMapping(value = "/logonAction.do", method = RequestMethod.GET)
    public ModelAndView logonActionGet() {
        System.out.println("[User004Controller::logonAction GET] start");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/logon.do"); // 기존 흐름 유지
        System.out.println("[User004Controller::logonAction GET] end\n");
        return mv;
    }

    // logonAction.do POST : 인증 처리
    @RequestMapping(value = "/logonAction.do", method = RequestMethod.POST)
    public ModelAndView logonActionPost(@ModelAttribute("User") User user,
                                        HttpSession session,
                                        HttpServletRequest request) {

        System.out.println("[User004Controller::logonAction POST] start");

        String viewName = "/user002/logon.jsp";

        // DAO 인증
        UserDAO userDAO = new UserDAO();
        userDAO.getUser(user); // 내부에서 user.active, userId 등 채워진다고 가정

        if (user.isActive()) {
            // 인증 성공 : 세션 확정
            session.setAttribute("sessionUser", user);
            request.setAttribute("loggedIn", true); // 다음 뷰/요청에서 활용 가능
            viewName = "/user002/home.jsp";
        }

        String message = viewName.equals("/user002/home.jsp")
                ? "[logonAction()] Welcome"
                : "[logonAction()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName(viewName);
        mv.addObject("message", message);

        System.out.println("[logonAction POST] active=" + user.isActive() + ", view=" + viewName);
        System.out.println("[User004Controller::logonAction POST] end\n");
        return mv;
    }

    // 로그아웃 : 세션 사용자 제거 후 로그인 페이지
    @RequestMapping("/logout.do")
    public ModelAndView logout(HttpSession session) {

        System.out.println("[User004Controller::logout()] start");

        session.removeAttribute("sessionUser");

        String message = "[logout()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user002/logon.jsp");
        mv.addObject("message", message);

        System.out.println("[logout] view=/user002/logon.jsp");
        System.out.println("[User004Controller::logout()] end\n");
        return mv;
    }
}
