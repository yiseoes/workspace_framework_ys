package spring.web.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import spring.domain.User;
import spring.service.user.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	public UserController() {
		System.out.println("==> UserController default Constructor call.............");
	}

	// 단순 Navigation (로그인 폼 이동)
	@RequestMapping("/logon")
	public String logon(Model model) {
		System.out.println("\n:: ==> logon() start....");
		String message = "[logon()] 아이디, 패스워드 3자 이상 입력하세요.";
		model.addAttribute("message", message);
		System.out.println(":: ==> logon() end....");
		return "/user/logon.jsp";
	}

	// Home 이동//
	@RequestMapping("/home")
	public String home(HttpSession session, Model model) {
		System.out.println("\n:: ==> home() start....");
		String message = "[home()] 환영합니다.";
		model.addAttribute("message", message);
		System.out.println(":: ==> home() end....");
		return "/user/home.jsp";
	}

	// logonAction (GET 요청시 -> logon 페이지로 이동)
	@RequestMapping(value = "/logonAction", method = RequestMethod.GET)
	public String logonActionGet() {
		System.out.println("\n:: ==> logonAction() GET start....");
		System.out.println(":: ==> logonAction() GET end....");
		// GET 요청은 단순히 로그인 폼으로 보냄
		return "redirect:/user/logon";
	}

	// logonAction (POST 요청시 -> 로그인 검증)
	@RequestMapping(value = "/logonAction", method = RequestMethod.POST)
	public String logonActionPost(@ModelAttribute("user") User user, HttpSession session, Model model)
			throws Exception {

		System.out.println("\n:: ==> logonAction() POST start....");

		String viewName = "/user/logon.jsp";
		String message = "[logonAction()] 아이디 또는 비밀번호가 올바르지 않습니다.";

		User returnUser = userService.getUser(user.getUserId());

		if (returnUser != null && returnUser.getPassword().equals(user.getPassword())) {
			returnUser.setActive(true);
			session.setAttribute("sessionUser", returnUser);
			viewName = "/user/home.jsp";
			message = "[logonAction()] WELCOME!";
		}

		model.addAttribute("message", message);

		System.out.println(" [action : " + viewName + "]");
		System.out.println(":: ==> logonAction() POST end....");

		return viewName;
	}

	// 로그아웃
	@RequestMapping("/logout")
	public String logout(HttpSession session, Model model) {
		System.out.println("\n:: ==> logout() start....");

		session.removeAttribute("sessionUser");
		String message = "[logout()] 로그아웃 되었습니다.";
		model.addAttribute("message", message);

		System.out.println(":: ==> logout() end....");

		return "/user/logon.jsp";
	}
}
