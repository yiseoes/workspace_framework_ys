package com.model2.mvc.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model2.mvc.service.domain.User;


/*
 * FileName : LogonCheckInterceptor.java
 *  ㅇ Controller 호출전 interceptor 를 통해 선처리/후처리/완료처리를 수행
 *  	- preHandle() : Controller 호출전 선처리   
 * 			(true return ==> Controller 호출 / false return ==> Controller 미호출 ) 
 *  	- postHandle() : Controller 호출 후 후처리
 *    	- afterCompletion() : view 생성후 처리
 *    
 *    ==> 로그인한 회원이면 Controller 호출 : true return
 *    ==> 비 로그인한 회원이면 Controller 미 호출 : false return
 */
public class LogonCheckInterceptor extends HandlerInterceptorAdapter {

	///Field
	
	///Constructor
	public LogonCheckInterceptor(){
		System.out.println("\nCommon :: "+this.getClass()+"\n");		
	}
	
	///Method
	public boolean preHandle(	HttpServletRequest request,
														HttpServletResponse response, 
														Object handler) throws Exception {
		
		System.out.println("\n[ LogonCheckInterceptor start........]");
		
		//==> 로그인 유무확인
		HttpSession session = request.getSession(true);

		// 08 혼재 프로젝트 대응 : user 없으면 userVO 사용
		User user = (User)( session.getAttribute("user") != null
											 ? session.getAttribute("user")
											 : session.getAttribute("userVO") );

		//==> 로그인한 회원이라면...
		if( user != null )  {

			String uri = request.getRequestURI();

			//==> 로그인 상태에서 접근 불가 URI (회원가입/로그인 관련)
			if(		uri.indexOf("addUserView") != -1 	|| 	uri.indexOf("addUser") != -1 || 
					uri.indexOf("loginView") != -1 			||	uri.indexOf("login") != -1 		|| 
					uri.indexOf("checkDuplication") != -1 ){
				request.getRequestDispatcher("/index.jsp").forward(request, response);
				System.out.println("[ 로그인 상태.. 로그인 후 불필요 한 요구.... ]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
				return false;
			}

			// ===== [관리자 판정] =====
			boolean isAdmin =
				"admin".equalsIgnoreCase( (user.getUserId()==null?"":user.getUserId().trim()) )
			 || "admin".equalsIgnoreCase( (user.getRole()==null?"":user.getRole().trim()) )
			 || "role_admin".equalsIgnoreCase( (user.getRole()==null?"":user.getRole().trim()) )
			 || "a".equalsIgnoreCase( (user.getRole()==null?"":user.getRole().trim()) );

			// ===== [관리자 전용 자원 접근 제어] =====
			// 1) 상품 등록/수정 (컨트롤러 매핑 기준 : addProduct / updateProduct)
			if(		uri.indexOf("addProductView") != -1  // 과거 경로 호환
				 || uri.indexOf("addProduct") != -1
				 || uri.indexOf("updateProductView") != -1 // 과거 경로 호환
				 || uri.indexOf("updateProduct") != -1 ) {

				if( !isAdmin ){
					request.setAttribute("message", "관리자만 접근 가능합니다.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					System.out.println("[ 차단 ] 관리자 전용 자원(상품 등록/수정)에 비관리자 접근 : uri="+uri);
					System.out.println("[ LogonCheckInterceptor end........]\n");
					return false;
				}
			}

			// 2) 판매상품 관리 화면 : /product/listProduct?menu=manage
			if( uri.indexOf("/product/listProduct") != -1 ) {
				String menu = request.getParameter("menu");
				if( "manage".equalsIgnoreCase(menu) && !isAdmin ) {
					request.setAttribute("message", "관리자만 접근 가능합니다.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					System.out.println("[ 차단 ] 관리자 전용 자원(판매상품관리)에 비관리자 접근 : uri="+uri+", menu="+menu);
					System.out.println("[ LogonCheckInterceptor end........]\n");
					return false;
				}
			}

			// 3) 퍼처스에서 상품 기준 배송상태 일괄 변경 : /purchase/updateTranCodeByProd
			if( uri.indexOf("/purchase/updateTranCodeByProd") != -1 ) {
				if( !isAdmin ) {
					request.setAttribute("message", "관리자만 접근 가능합니다.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					System.out.println("[ 차단 ] 관리자 전용 자원(배송상태 일괄 변경)에 비관리자 접근 : uri="+uri);
					System.out.println("[ LogonCheckInterceptor end........]\n");
					return false;
				}
			}

			// (선택) REST 관리자 액션 보호 : /product/json/addProduct|updateProduct, /product/file/*
			// 운영 중 REST 사용 시 주석 해제 고려
			/*
			if( uri.indexOf("/product/json/addProduct") != -1
			 || uri.indexOf("/product/json/updateProduct") != -1
			 || uri.indexOf("/product/file/") != -1 ) {
				if( !isAdmin ){
					request.setAttribute("message", "관리자만 접근 가능합니다.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					System.out.println("[ 차단 ] 관리자 전용 자원(REST 파일/등록/수정)에 비관리자 접근 : uri="+uri);
					System.out.println("[ LogonCheckInterceptor end........]\n");
					return false;
				}
			}
			*/

			System.out.println("[ 로그인 상태 ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
			return true;

		}else{ //==> 미 로그인한 회원이라면...
			String uri = request.getRequestURI();

			//==> 로그인/회원가입 관련은 통과
			if(		uri.indexOf("addUserView") != -1 	|| 	uri.indexOf("addUser") != -1 || 
					uri.indexOf("loginView") != -1 			||	uri.indexOf("login") != -1 		|| 
					uri.indexOf("checkDuplication") != -1 ){
				System.out.println("[ 로그 시도 상태 .... ]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
				return true;
			}

			// 필요 시 : 비로그인 허용 자원 추가(예: 메인, 공지 등)
			// 현재 정책 유지 : 그 외는 로그인 필요
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			System.out.println("[ 로그인 이전 ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
			return false;
		}
	}
}//end of class
