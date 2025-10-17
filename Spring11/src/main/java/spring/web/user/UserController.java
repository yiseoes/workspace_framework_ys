package spring.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import spring.domain.User;
import spring.service.user.UserService;

/**
 * User004Controller
 * - 인터셉터가 sessionUser 보장 및 loggedIn 플래그 전달
 * - 컨트롤러는 화면 네비게이션만 담당
 * - JSP 경로 : /user002/logon.jsp, /user002/home.jsp
 */
@Controller
public class UserController {
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

    public UserController() {
        System.out.println("==> UserController default Constructor call.............");
    }

    // 로그인 화면 / 로그인 시 홈
    @RequestMapping("/logon.do")
    public ModelAndView logon(@RequestAttribute(name = "loggedIn", required = false) Boolean loggedIn,
                              HttpSession session) {

        System.out.println("[UserController::logon()] start");

        // 인터셉터가 보장하지만 방어적으로 처리
        if (loggedIn == null) {
            Object su = session.getAttribute("sessionUser");
            loggedIn = (su instanceof User) && ((User) su).isActive();
        }

        String viewName = loggedIn ? "/user/home.jsp" : "/user/logon.jsp";
        String message  = loggedIn ? "[logon()] Welcome" : "[logon()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName(viewName);
        mv.addObject("message", message);

        System.out.println("[logon] loggedIn=" + loggedIn + ", view=" + viewName);
        System.out.println("[UserController::logon()] end\n");
        return mv;
    }

    // 홈 / 미로그인 시 로그인 페이지
    @RequestMapping("/home.do")
    public ModelAndView home(@RequestAttribute(name = "loggedIn", required = false) Boolean loggedIn,
                             HttpSession session) {

        System.out.println("[UserController::home()] start");

        if (loggedIn == null) {
            Object su = session.getAttribute("sessionUser");
            loggedIn = (su instanceof User) && ((User) su).isActive();
        }

        String viewName = loggedIn ? "/user/home.jsp" : "/user/logon.jsp";
        String message  = loggedIn ? "[home()] Welcome" : "[home()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName(viewName);
        mv.addObject("message", message);

        System.out.println("[home] loggedIn=" + loggedIn + ", view=" + viewName);
        System.out.println("[UserController::home()] end\n");
        return mv;
    }

    // logonAction.do GET : 폼 없이 접근할 때 로그인 페이지로 유도
    @RequestMapping(value = "/logonAction.do", method = RequestMethod.GET)
    public ModelAndView logonActionGet() {
        System.out.println("[UserController::logonAction GET] start");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/logon.do"); // 기존 흐름 유지
        System.out.println("[UserController::logonAction GET] end\n");
        return mv;
    }

    @RequestMapping(value = "/logonAction.do", method = RequestMethod.POST)
    public ModelAndView logonActionPost(@ModelAttribute("User") User user,
                                        HttpSession session,
                                        HttpServletRequest request) throws Exception {
        
        System.out.println("[UserController::logonAction POST] start");

        String viewName = "/user/logon.jsp";

        User returnUser = userService.getUser(user.getUserId());
        if(returnUser.getPassword().equals(user.getPassword())) {
            returnUser.setActive(true);
            user = returnUser;
        }

        if (user.isActive()) {
            session.setAttribute("sessionUser", user);
            request.setAttribute("loggedIn", true);
            viewName = "/user/home.jsp";
        }

        String message = viewName.equals("/user/home.jsp")
                ? "[logonAction()] Welcome"
                : "[logonAction()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName(viewName);
        mv.addObject("message", message);

        System.out.println("[logonAction POST] active=" + user.isActive() + ", view=" + viewName);
        System.out.println("[UserController::logonAction POST] end\n");
        return mv;
    }


    // 로그아웃 : 세션 사용자 제거 후 로그인 페이지
    @RequestMapping("/logout.do")
    public ModelAndView logout(HttpSession session) {

        System.out.println("[UserController::logout()] start");

        session.removeAttribute("sessionUser");

        String message = "[logout()] 아이디/패스워드 3자이상 입력하세요.";

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user/logon.jsp");
        mv.addObject("message", message);

        System.out.println("[logout] view=/user/logon.jsp");
        System.out.println("[UserController::logout()] end\n");
        return mv;
    }
}
