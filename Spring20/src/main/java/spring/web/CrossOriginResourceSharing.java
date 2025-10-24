package spring.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class Test
 */
//@WebFilter("/Test")
public class CrossOriginResourceSharing extends HttpFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		System.out.println("CrossOriginResourceSharing.doFilter()");
//=============================================================//
//==> HttpSession(내부적으로 cookie 사용) 기반 상태유지 위해 아래 와 같이 변경		
//=============================================================//
//		HttpServletResponse res = (HttpServletResponse) response; 
//		res.setHeader("Access-Control-Allow-Origin", "*"); //허용대상 도메인
//=============================================================//		
	
		//==> 변경된 부위
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
		
		String origin = req.getHeader("Origin");  //==> 요청이 어디서 오는지 확인.
		System.out.println(origin);   //==> 같은 도메인은 null 아니면 / 다른 ip:port 나옴. 
		
		if (origin != null) {
		    res.setHeader("Access-Control-Allow-Origin", origin);           // 요청 origin 허용대상 도메인 설정
		    res.setHeader("Access-Control-Allow-Credentials", "true"); // 쿠키를 내려보내는 것 허용
		}
//=============================================================//		

		res.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT"); 
		res.setHeader("Access-Control-Max-Age", "3600"); 
		res.setHeader("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept"); 

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("CrossOriginResourceSharing.init()");
	}

}
