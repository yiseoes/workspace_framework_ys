package com.model2.mvc.web.user;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// 추가 import
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;

//추가 import (파일 상단에 배치)
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/user/*")
public class UserController {
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
		
	public UserController(){
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	// (기존 addUser, getUser, updateUser 등 메소드는 그대로 유지...)
	@RequestMapping( value="addUser", method=RequestMethod.GET )
	public String addUser() throws Exception{
		System.out.println("/user/addUser : GET");
		return "redirect:/user/addUserView.jsp";
	}
	@RequestMapping( value="addUser", method=RequestMethod.POST )
	public String addUser( @ModelAttribute("user") User user ) throws Exception {
		System.out.println("/user/addUser : POST");
		userService.addUser(user);
		return "redirect:/user/loginView.jsp";
	}
	@RequestMapping( value="getUser", method=RequestMethod.GET )
	public String getUser( @RequestParam("userId") String userId , Model model ) throws Exception {
		System.out.println("/user/getUser : GET");
		User user = userService.getUser(userId);
		model.addAttribute("user", user);
		return "forward:/user/getUser.jsp";
	}
	@RequestMapping( value="updateUser", method=RequestMethod.GET )
	public String updateUser( @RequestParam("userId") String userId , Model model ) throws Exception{
		System.out.println("/user/updateUser : GET");
		User user = userService.getUser(userId);
		model.addAttribute("user", user);
		return "forward:/user/updateUser.jsp";
	}
	@RequestMapping( value="updateUser", method=RequestMethod.POST )
	public String updateUser( @ModelAttribute("user") User user , Model model , HttpSession session) throws Exception{
		System.out.println("/user/updateUser : POST");
		userService.updateUser(user);
		String sessionId=((User)session.getAttribute("user")).getUserId();
		if(sessionId.equals(user.getUserId())){
			session.setAttribute("user", user);
		}
		return "redirect:/user/getUser?userId="+user.getUserId();
	}
	@RequestMapping( value="login", method=RequestMethod.GET )
	public String login() throws Exception{
		System.out.println("/user/logon : GET");
		return "redirect:/user/loginView.jsp";
	}
	@RequestMapping( value="login", method=RequestMethod.POST )
	public String login(@ModelAttribute("user") User user , HttpSession session ) throws Exception{
		System.out.println("/user/login : POST");
		User dbUser=userService.getUser(user.getUserId());
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		return "redirect:/index.jsp";
	}
	@RequestMapping( value="logout", method=RequestMethod.GET )
	public String logout(HttpSession session ) throws Exception{
		System.out.println("/user/logout : POST");
		session.invalidate();
		return "redirect:/index.jsp";
	}
	@RequestMapping( value="checkDuplication", method=RequestMethod.POST )
	public String checkDuplication( @RequestParam("userId") String userId , Model model ) throws Exception{
		System.out.println("/user/checkDuplication : POST");
		boolean result=userService.checkDuplication(userId);
		model.addAttribute("result", new Boolean(result));
		model.addAttribute("userId", userId);
		return "forward:/user/checkDuplication.jsp";
	}
	@RequestMapping( value="listUser" )
	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		System.out.println("/user/listUser : GET / POST");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		Map<String , Object> map=userService.getUserList(search);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		return "forward:/user/listUser.jsp";
	}
	
	// =================================================================
	// ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 소셜 로그인 관련 코드 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	// =================================================================

	// --------------------------------- 카카오 ---------------------------------
//	@RequestMapping(value = "/kakao/callback", method = RequestMethod.GET)
//	public String kakaoCallback(@RequestParam String code, HttpSession session) throws Exception {
//		System.out.println("==> [UserController] /kakao/callback : GET 요청 받음, 인증코드=" + code);
//		String accessToken = getKakaoAccessToken(code);
//		Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
//		if (userInfo != null && userInfo.get("email") != null) {
//			processSocialLogin((String) userInfo.get("email"), (String) userInfo.get("nickname"), session);
//		}
//		return "redirect:/index.jsp";
//	}
//	private String getKakaoAccessToken(String code) throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("grant_type", "authorization_code");
//		params.add("client_id", "cf296aed85af05aab502785a1c9d6497");
//		params.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
//		params.add("code", code);
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, request, String.class);
//		Map<String, Object> jsonMap = new ObjectMapper().readValue(response.getBody(), Map.class);
//		return (String) jsonMap.get("access_token");
//	}
//	private Map<String, Object> getKakaoUserInfo(String accessToken) throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Authorization", "Bearer " + accessToken);
//		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, request, String.class);
//		Map<String, Object> jsonMap = new ObjectMapper().readValue(response.getBody(), Map.class);
//		Map<String, Object> accountInfo = (Map<String, Object>) jsonMap.get("kakao_account");
//		Map<String, Object> profileInfo = (Map<String, Object>) accountInfo.get("profile");
//		Map<String, Object> userInfo = new java.util.HashMap<>();
//		userInfo.put("email", accountInfo.get("email"));
//		userInfo.put("nickname", profileInfo.get("nickname"));
//		return userInfo;
//	}
//
//	// --------------------------------- 구글 ---------------------------------
//	@RequestMapping(value = "/google/callback", method = RequestMethod.GET)
//	public String googleCallback(@RequestParam String code, HttpSession session) throws Exception {
//		System.out.println("==> [UserController] /google/callback : GET 요청 받음, 인증코드=" + code);
//		String accessToken = getGoogleAccessToken(code);
//		Map<String, Object> userInfo = getGoogleUserInfo(accessToken);
//		if (userInfo != null && userInfo.get("email") != null) {
//			processSocialLogin((String) userInfo.get("email"), (String) userInfo.get("name"), session);
//		}
//		return "redirect:/index.jsp";
//	}
//	private String getGoogleAccessToken(String code) throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("grant_type", "authorization_code");
//		params.add("client_id", "319852921219-s1a5ljc9l9p4v1jngk2np8u4o0f4d5ih.apps.googleusercontent.com");
//		params.add("client_secret", "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");
//		params.add("redirect_uri", "http://127.0.0.1:8080");
//		params.add("code", code);
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST, request, String.class);
//		Map<String, Object> jsonMap = new ObjectMapper().readValue(response.getBody(), Map.class);
//		return (String) jsonMap.get("access_token");
//	}
//	private Map<String, Object> getGoogleUserInfo(String accessToken) throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Authorization", "Bearer " + accessToken);
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, request, String.class);
//		return new ObjectMapper().readValue(response.getBody(), Map.class);
//	}
//

	
	// =================================================================
	// ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 소셜 로그인 관련 코드 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	// =================================================================

	// --------------------------------- 카카오 ---------------------------------

	@RequestMapping(value = "/kakao/callback", method = RequestMethod.GET)
	public String kakaoCallback(@RequestParam String code, HttpSession session) throws Exception {
	    System.out.println("[KAKAO][콜백] 인가코드 수신 완료 : code=" + code);

	    String accessToken = getKakaoAccessToken(code);
	    System.out.println("[KAKAO][콜백] 액세스 토큰 발급 성공 : accessToken(앞 12자리)=" 
	                        + (accessToken != null ? accessToken.substring(0, Math.min(12, accessToken.length())) + "..." : null));

	    Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
	    System.out.println("[KAKAO][콜백] 사용자 정보 파싱 결과 : " + userInfo);

	    // [식별성 주기 카카오:k-]
	    Object email = userInfo != null ? userInfo.get("email") : null;
	    Object id    = userInfo != null ? userInfo.get("id")    : null;
	    Object nick  = userInfo != null ? userInfo.get("nickname") : null;

	    // 기본 식별자(이메일이면 소문자 정규화, 없으면 kakao_+id)
	    String baseId = (email != null) ? String.valueOf(email).toLowerCase()
	                                    : "kakao_" + String.valueOf(id);

	    // 플랫폼 접두어(k-)
	    String userIdForLogin   = "k-" + baseId;
	    String userNameForLogin = (nick != null) ? String.valueOf(nick) : "KakaoUser";

	    System.out.println("[KAKAO][콜백] 로그인 식별자 선택 : userId=" + userIdForLogin + ", userName=" + userNameForLogin);
	    processSocialLogin(userIdForLogin, userNameForLogin, session);

	    System.out.println("[KAKAO][콜백] 소셜 로그인 처리 완료 → index.jsp 리다이렉트");
	    return "redirect:/index.jsp";
	}

	private String getKakaoAccessToken(String code) throws Exception {
	    System.out.println("[KAKAO][토큰] 교환 요청 시작");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	    // 동적 redirect_uri
	    javax.servlet.http.HttpServletRequest _req =
	        ((org.springframework.web.context.request.ServletRequestAttributes)
	            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
	    String _base = _req.getScheme() + "://" + _req.getServerName() + ":" + _req.getServerPort() + _req.getContextPath();
	    String redirectUri = _base + "/user/kakao/callback";

	    org.springframework.util.MultiValueMap<String, String> params = new org.springframework.util.LinkedMultiValueMap<String, String>();
	    params.add("grant_type", "authorization_code");
	    params.add("client_id", "cf296aed85af05aab502785a1c9d6497");
	    params.add("redirect_uri", redirectUri);
	    params.add("code", code);

	    System.out.println("[KAKAO][토큰] 요청 파라미터 확인 : redirect_uri=" + redirectUri + ", code=" + code);

	    HttpEntity<org.springframework.util.MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	                "https://kauth.kakao.com/oauth/token",
	                HttpMethod.POST, request, String.class);

	        System.out.println("[KAKAO][토큰] HTTP 상태코드=" + response.getStatusCode());
	        System.out.println("[KAKAO][토큰] 응답 바디=" + response.getBody());

	        Map<String, Object> jsonMap = new ObjectMapper().readValue(response.getBody(), Map.class);
	        String token = (String) jsonMap.get("access_token");
	        System.out.println("[KAKAO][토큰] 액세스 토큰 파싱 완료");
	        return token;

	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        System.out.println("[KAKAO][토큰][오류] HTTP 상태코드=" + e.getStatusCode());
	        System.out.println("[KAKAO][토큰][오류] 응답 바디=" + e.getResponseBodyAsString());
	        System.out.println("[KAKAO][토큰][가이드] KOE006/invalid_grant → redirect_uri 불일치, code 재사용/만료, 호스트 불일치(localhost ↔ 127.0.0.1) 점검");
	        throw e;
	    }
	}

	private Map<String, Object> getKakaoUserInfo(String accessToken) throws Exception {
	    System.out.println("[KAKAO][유저] 조회 요청 시작");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + accessToken);
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	    HttpEntity<Void> request = new HttpEntity<>(headers);
	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	                "https://kapi.kakao.com/v2/user/me",
	                HttpMethod.POST, request, String.class);

	        System.out.println("[KAKAO][유저] HTTP 상태코드=" + response.getStatusCode());
	        System.out.println("[KAKAO][유저] 응답 바디=" + response.getBody());

	        Map<String, Object> json = new ObjectMapper().readValue(response.getBody(), Map.class);
	        Map<String, Object> account = (Map<String, Object>) json.get("kakao_account");
	        Map<String, Object> profile = (account != null) ? (Map<String, Object>) account.get("profile") : null;

	        Map<String, Object> userInfo = new java.util.HashMap<>();
	        userInfo.put("id", json.get("id"));
	        userInfo.put("email", (account != null) ? account.get("email") : null);
	        userInfo.put("nickname", (profile != null) ? profile.get("nickname") : null);

	        System.out.println("[KAKAO][유저] 파싱 결과 요약 : id=" + userInfo.get("id") 
	                           + ", email=" + userInfo.get("email") 
	                           + ", nickname=" + userInfo.get("nickname"));
	        return userInfo;

	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        System.out.println("[KAKAO][유저][오류] HTTP 상태코드=" + e.getStatusCode());
	        System.out.println("[KAKAO][유저][오류] 응답 바디=" + e.getResponseBodyAsString());
	        System.out.println("[KAKAO][유저][가이드] 401/unauthorized → Authorization 헤더/토큰 유효성 확인");
	        throw e;
	    }
	}

	// --------------------------------- 구글 ---------------------------------
	@RequestMapping(value = "/google/callback", method = RequestMethod.GET)
	public String googleCallback(@RequestParam String code, HttpSession session) throws Exception {
	    System.out.println("[GOOGLE][콜백] 인가코드 수신 : code=" + code);

	    String accessToken = getGoogleAccessToken(code);
	    System.out.println("[GOOGLE][콜백] 액세스 토큰 발급 성공(앞 12자리) = "
	            + (accessToken != null ? accessToken.substring(0, Math.min(12, accessToken.length())) + "..." : null));

	    Map<String, Object> userInfo = getGoogleUserInfo(accessToken);
	    System.out.println("[GOOGLE][콜백] 유저정보 = " + userInfo);
	    
	    // 구글 식별성 주기 :g-
	    if (userInfo != null && userInfo.get("email") != null) {
	        String baseId = String.valueOf(userInfo.get("email")).toLowerCase(); // 소문자 정규화
	        String userIdForLogin   = "g-" + baseId;                              // 접두어
	        String userNameForLogin = String.valueOf(userInfo.getOrDefault("name", "GoogleUser"));
	        processSocialLogin(userIdForLogin, userNameForLogin, session);
	    } else {
	        System.out.println("[GOOGLE][콜백][경고] email 값이 없음 → 로그인 중단");
	    }

	    return "redirect:/index.jsp";
	}

	private String getGoogleAccessToken(String code) throws Exception {
	    System.out.println("[GOOGLE][토큰] 교환 요청 시작");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	    // 동적 redirect_uri
	    javax.servlet.http.HttpServletRequest _req =
	        ((org.springframework.web.context.request.ServletRequestAttributes)
	            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
	    String _base = _req.getScheme() + "://" + _req.getServerName() + ":" + _req.getServerPort() + _req.getContextPath();
	    String redirectUri = _base + "/user/google/callback";

	    org.springframework.util.MultiValueMap<String, String> params = new org.springframework.util.LinkedMultiValueMap<String, String>();
	    params.add("grant_type", "authorization_code");
	    params.add("client_id", "516891552299-0dhkpa2dm2cls435lv832farshtb5m0h.apps.googleusercontent.com");
	    params.add("client_secret", "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");
	    params.add("redirect_uri", redirectUri);
	    params.add("code", code);

	    HttpEntity<org.springframework.util.MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        ResponseEntity<String> res = restTemplate.exchange(
	                "https://oauth2.googleapis.com/token", HttpMethod.POST, request, String.class);
	        System.out.println("[GOOGLE][토큰] HTTP=" + res.getStatusCode());
	        System.out.println("[GOOGLE][토큰] 응답=" + res.getBody());

	        Map<String, Object> json = new ObjectMapper().readValue(res.getBody(), Map.class);
	        return (String) json.get("access_token");
	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        System.out.println("[GOOGLE][토큰][오류] HTTP=" + e.getStatusCode());
	        System.out.println("[GOOGLE][토큰][오류] 바디=" + e.getResponseBodyAsString());
	        System.out.println("[GOOGLE][토큰][가이드] 400/invalid_grant → redirect_uri 불일치, code 재사용/만료, 테스트사용자 미등록 점검");
	        throw e;
	    }
	}

	private Map<String, Object> getGoogleUserInfo(String accessToken) throws Exception {
	    System.out.println("[GOOGLE][유저] 조회 요청 시작");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + accessToken);
	    HttpEntity<Void> request = new HttpEntity<>(headers);
	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        ResponseEntity<String> res = restTemplate.exchange(
	                "https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, request, String.class);
	        System.out.println("[GOOGLE][유저] HTTP=" + res.getStatusCode());
	        System.out.println("[GOOGLE][유저] 응답=" + res.getBody());

	        return new ObjectMapper().readValue(res.getBody(), Map.class);
	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        System.out.println("[GOOGLE][유저][오류] HTTP=" + e.getStatusCode());
	        System.out.println("[GOOGLE][유저][오류] 바디=" + e.getResponseBodyAsString());
	        System.out.println("[GOOGLE][유저][가이드] 401/invalid_token → Authorization 헤더/토큰 만료 확인");
	        throw e;
	    }
	}

	// --------------------------------- 네이버 시작(인가요청) ---------------------------------
	@RequestMapping(value = "/naver/login", method = RequestMethod.GET)
	public String naverLogin(HttpSession session) throws Exception {
	    System.out.println("[NAVER][시작] 네이버 인가요청 준비");

	    // state 생성 및 세션 저장(필수)
	    String state = UUID.randomUUID().toString();
	    session.setAttribute("NAVER_STATE", state);
	    System.out.println("[NAVER][시작] 생성한 state=" + state);

	    // 동적 redirect_uri
	    javax.servlet.http.HttpServletRequest _req =
	        ((org.springframework.web.context.request.ServletRequestAttributes)
	            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
	    String _base = _req.getScheme() + "://" + _req.getServerName() + ":" + _req.getServerPort() + _req.getContextPath();
	    String redirectUri = _base + "/user/naver/callback";

	    String clientId = "MeTokB6p_xyzzSlYR3uJ"; // 네이버 콘솔 Client ID

	    String authorize = "https://nid.naver.com/oauth2.0/authorize"
	            + "?response_type=code"
	            + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8.name())
	            + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name())
	            + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8.name())
	            + "&auth_type=reprompt";

	    System.out.println("[NAVER][시작] 리다이렉트 → " + authorize);
	    return "redirect:" + authorize;
	}

	// --------------------------------- 네이버 콜백 ---------------------------------
	@RequestMapping(value = "/naver/callback", method = RequestMethod.GET)
	public String naverCallback(@RequestParam String code,
	                            @RequestParam String state,
	                            HttpSession session) throws Exception {
	    System.out.println("[NAVER][콜백] 인가코드 수신 : code=" + code + ", state=" + state);

	    // state 검증(필수)
	    Object saved = session.getAttribute("NAVER_STATE");
	    if (saved == null || !state.equals(saved.toString())) {
	        System.out.println("[NAVER][콜백][오류] state 불일치 → 요청 위변조 가능성");
	        return "redirect:/index.jsp";
	    }
	    System.out.println("[NAVER][콜백] state 검증 완료");

	    String accessToken = getNaverAccessToken(code, state);
	    System.out.println("[NAVER][콜백] 액세스 토큰 발급 성공(앞 12자리) = "
	            + (accessToken != null ? accessToken.substring(0, Math.min(12, accessToken.length())) + "..." : null));

	    Map<String, Object> userInfo = getNaverUserInfo(accessToken);
	    System.out.println("[NAVER][콜백] 사용자 정보 = " + userInfo);

	    // [식별성 주기 네이버: n-]
	    Object email = userInfo.get("email");
	    Object id    = userInfo.get("id");
	    Object name  = userInfo.get("name");

	    String baseId = (email != null) ? String.valueOf(email).toLowerCase()
	                                    : "naver_" + String.valueOf(id);

	    String userIdForLogin   = "n-" + baseId;
	    String userNameForLogin = (name != null) ? String.valueOf(name) : "NaverUser";

	    System.out.println("[NAVER][콜백] 로그인 식별자 : userId=" + userIdForLogin + ", userName=" + userNameForLogin);
	    processSocialLogin(userIdForLogin, userNameForLogin, session);

	    System.out.println("[NAVER][콜백] 소셜 로그인 처리 완료 → index.jsp");
	    return "redirect:/index.jsp";
	}

	// --------------------------------- 네이버 토큰 교환 ---------------------------------
	private String getNaverAccessToken(String code, String state) throws Exception {
	    System.out.println("[NAVER][토큰] 교환 요청 시작");

	    String url = "https://nid.naver.com/oauth2.0/token";
	    String clientId = "MeTokB6p_xyzzSlYR3uJ";
	    String clientSecret = "hBCHaLnQLW";

	    // 동적 redirect_uri
	    javax.servlet.http.HttpServletRequest _req =
	        ((org.springframework.web.context.request.ServletRequestAttributes)
	            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
	    String _base = _req.getScheme() + "://" + _req.getServerName() + ":" + _req.getServerPort() + _req.getContextPath();
	    String redirectUri = _base + "/user/naver/callback";

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

	    org.springframework.util.MultiValueMap<String, String> params = new org.springframework.util.LinkedMultiValueMap<String, String>();
	    params.add("grant_type", "authorization_code");
	    params.add("client_id", clientId);
	    params.add("client_secret", clientSecret);
	    params.add("code", code);
	    params.add("state", state);
	    params.add("redirect_uri", redirectUri);

	    HttpEntity<org.springframework.util.MultiValueMap<String, String>> req = new HttpEntity<>(params, headers);
	    RestTemplate rt = new RestTemplate();

	    try {
	        ResponseEntity<String> res = rt.exchange(url, HttpMethod.POST, req, String.class);
	        System.out.println("[NAVER][토큰] HTTP=" + res.getStatusCode());
	        System.out.println("[NAVER][토큰] 응답=" + res.getBody());

	        Map<String, Object> map = new ObjectMapper().readValue(res.getBody(), Map.class);
	        return (String) map.get("access_token");
	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        System.out.println("[NAVER][토큰][오류] HTTP=" + e.getStatusCode());
	        System.out.println("[NAVER][토큰][오류] 바디=" + e.getResponseBodyAsString());
	        System.out.println("[NAVER][토큰][가이드] invalid_request/invalid_grant → redirect_uri 불일치, code 재사용/만료, state 누락/불일치");
	        throw e;
	    }
	}

	// --------------------------------- 네이버 유저 조회 ---------------------------------
	private Map<String, Object> getNaverUserInfo(String accessToken) throws Exception {
	    System.out.println("[NAVER][유저] 조회 요청 시작");

	    String url = "https://openapi.naver.com/v1/nid/me";

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + accessToken);

	    HttpEntity<Void> req = new HttpEntity<>(headers);
	    RestTemplate rt = new RestTemplate();

	    try {
	        ResponseEntity<String> res = rt.exchange(url, HttpMethod.GET, req, String.class);
	        System.out.println("[NAVER][유저] HTTP=" + res.getStatusCode());
	        System.out.println("[NAVER][유저] 응답=" + res.getBody());

	        Map<String, Object> map = new ObjectMapper().readValue(res.getBody(), Map.class);
	        Map<String, Object> resp = (Map<String, Object>) map.get("response"); // {"resultcode":"00","message":"success","response":{...}}

	        java.util.HashMap<String, Object> user = new java.util.HashMap<>();
	        if (resp != null) {
	            user.put("id", resp.get("id"));
	            user.put("email", resp.get("email"));   // 동의/권한 없으면 null
	            user.put("name", resp.get("name"));
	            user.put("nickname", resp.get("nickname"));
	        }
	        System.out.println("[NAVER][유저] 파싱 결과 : " + user);
	        return user;

	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        System.out.println("[NAVER][유저][오류] HTTP=" + e.getStatusCode());
	        System.out.println("[NAVER][유저][오류] 바디=" + e.getResponseBodyAsString());
	        System.out.println("[NAVER][유저][가이드] 401/invalid_token → Authorization 헤더/토큰 만료 확인");
	        throw e;
	    }
	}

	// --------------------------------- 공통 ---------------------------------
	private void processSocialLogin(String userId, String userName, HttpSession session) throws Exception {
	    System.out.println("[공통로그인] 시작 : userId=" + userId + ", userName=" + userName);

	    User existingUser = null;
	    try {
	        existingUser = userService.getUser(userId);
	        System.out.println("[공통로그인] 기존 회원 조회 결과 : " + (existingUser != null ? "존재" : "없음"));
	    } catch (Exception e) {
	        System.out.println("[공통로그인] 기존 회원 조회 중 예외 발생(신규로 간주) : " + e.getMessage());
	    }

	    if (existingUser != null) {
	        session.setAttribute("user", existingUser);
	        System.out.println("[공통로그인] 기존 회원 로그인 완료");
	    } else {
	        User newUser = new User();
	        newUser.setUserId(userId);
	        newUser.setUserName(userName);
	        newUser.setPassword("snslogin");
	        newUser.setRole("user");
	        userService.addUser(newUser);
	        session.setAttribute("user", newUser);
	        System.out.println("[공통로그인] 신규 가입 및 로그인 완료");
	    }
	}
	// =================================================================
	// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ 소셜 로그인 관련 코드 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
	// =================================================================
}