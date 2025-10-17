package spring.web.user;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import spring.domain.User;
import spring.service.user.impl.UserDAO;

/*
 *  FileName : User001Controller.java
 *  - Controller Coding Policy 
 *  	: return type : ModelAndView 적용 
 *  	: Method parameter  
 *  		- Client Data : @ModelAttribute / @RequestParam 적절이 사용
 *  	    - 필요시 : Servlet API 사용
 */
//@Controller
public class User002Controller {
	
	///Field
	
	///Constructor
	public User002Controller(){
		System.out.println("==> User001Controller default Constructor call.............");
	}
	
	// 단순 Navigation / Business Logic 수행없음
	@RequestMapping("/logon.do")
	public ModelAndView logon(HttpSession session){
		
		System.out.println("[:: ==> logon() start....]");
		
		if(session.isNew() || session.getAttribute("sessionUser") == null){
			session.setAttribute("sessionUser",new User());
		}	
		
		User sessionUser =(User)session.getAttribute("sessionUser");

		//==> CONTROLLER :: Navigation (forward/sendRedirect view page 결정)
		// Navigation 디폴트 페이지 지정
		String viewName = "/user002/logon.jsp";
		
		//==>UserVO.active  이용 로그인 유무판단 
		if( sessionUser.isActive() ){
			viewName = "/user002/home.jsp";
		}
		
		System.out.println("[action : "+viewName+"]");
		
		String message = null;
		if( viewName.equals("/user002/home.jsp")) {
			message = "[logon()] Welcome";
		}else {
			message = "[logon()] 아이디/패스워드 3자이상 입력하세요.";
		}
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( viewName );
		modelAndView.addObject("message", message);
		
		System.out.println("[logon() 끝~~]\n");
		
		return modelAndView;
	}
	
	@RequestMapping("/home.do")
	public ModelAndView home(HttpSession session){
				
		System.out.println("[:: ==> home() start....]");
		
		if(session.isNew() || session.getAttribute("sessionUser") == null){
			session.setAttribute("sessionUser",new User());
		}	
		
		User sessionUser =(User)session.getAttribute("sessionUser");

		String viewName = "/user002/logon.jsp";
		
		if( sessionUser.isActive() ){
			viewName = "/user002/home.jsp";
		}
		
		System.out.println("[action : "+viewName+"]");
		
		String message = null;
		if( viewName.equals("/user002/home.jsp")) {
			message = "[home()] Welcome";
		}else {
			message = "[home()] 아이디/패스워드 3자이상 입력하세요.";
		}
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( viewName );
		modelAndView.addObject("message", message);
		
		System.out.println("[home() 끝~~]\n");
		
		return modelAndView;
	}
	
	

	@RequestMapping("/logonAction.do")
	public ModelAndView logonAction(@ModelAttribute("user") User user,
	                                   HttpSession session){
	    
	    System.out.println("[logonAction () start....]");

	    if(session.isNew() || session.getAttribute("sessionUser") == null){
	        session.setAttribute("sessionUser", user);
	    }

	    User sessionUser =(User)session.getAttribute("sessionUser");

	    String viewName = "/user002/logon.jsp";

	    if( sessionUser.isActive() ){
	        viewName = "/user002/home.jsp";
	    } else {

	        UserDAO userDAO = new UserDAO();
	        userDAO.getUser(user);


	        session.setAttribute("sessionUser", user);

	        if (user.isActive()) {
	            viewName = "/user002/home.jsp";
	        }
	    }

	    System.out.println("[action : "+viewName+"]");

	    String message = null;
	    if( viewName.equals("/user002/home.jsp")) {
	        message = "[logonAction()] Welcome";
	    } else {
	        message = "[logonAction()] 아이디/패스워드 3자이상 입력하세요.";
	    }

	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName( viewName );
	    modelAndView.addObject("message", message);

	    System.out.println("[logonAction() 끝~~]\n");

	    return modelAndView;
	}
	
	
	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpSession session){
	    
	    System.out.println("[logout () start....]");

	    session.removeAttribute("sessionUser");

	    String message = "[logout()] 아이디/패스워드 3자이상 입력하세요.";

	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName( "/user002/logon.jsp" );
	    modelAndView.addObject("message", message);

	    System.out.println("[logout()] 끝~~]\n");

	    return modelAndView;
	}

	
}