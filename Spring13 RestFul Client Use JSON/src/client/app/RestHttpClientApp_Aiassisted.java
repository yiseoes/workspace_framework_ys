package client.app;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import spring.domain.User;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.Map;

public class RestHttpClientApp_Aiassisted {
	
	// main Method
	public static void main(String[] args) throws Exception{
		
		////////////////////////////////////////////////////////////////////////////////////////////
		// 주석을 하나씩 풀며 실습 (필요한 것만 주석 해제해서 실행)
		////////////////////////////////////////////////////////////////////////////////////////////
		
//		System.out.println("\n====================================\n");
//		// 1.1 Http GET : JsonSimple 사용
//		RestHttpClientApp_Aiassisted.ReqeustHttpGet_UseJsonSimple();
		
//		System.out.println("\n====================================\n");
//		// 1.2 Http GET : CodeHaus(Jackson1) 사용
//		RestHttpClientApp_Aiassisted.ReqeustHttpGet_UseCodeHaus();
		
//		System.out.println("\n====================================\n");
//		// 2.1 Http POST : JsonSimple 사용
//		RestHttpClientApp_Aiassisted.ReqeustHttpPostData_UseJsonSimple();
		
		System.out.println("\n====================================\n");
		// 2.2 Http POST : CodeHaus(Jackson1) 사용
		RestHttpClientApp_Aiassisted.ReqeustHttpPostData_UseCodeHaus();
	}
	
	
	//================================================================//
	// 1.1 Http Protocol GET Request : JsonSimple 3rd party lib 사용
	public static void ReqeustHttpGet_UseJsonSimple() throws Exception {
	    String url = "http://127.0.0.1:8080/Spring13/app/userAPI/getUser?name=" + enc("홍길동") + "&age=10";

	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader("Accept", "application/json");
	    httpGet.setHeader("Content-Type", "application/json; charset=UTF-8");

	    HttpResponse response = client.execute(httpGet);
	    String body = EntityUtils.toString(response.getEntity(), "UTF-8");

	    System.out.println("\n[GET/getUser] 요청 시작");
	    System.out.println("요청 URL : " + url);
	    System.out.println("서버 응답(JSON) : " + body);

	    // JSON 파싱
	    JSONParser parser = new JSONParser();
	    JSONObject root = (JSONObject) parser.parse(body);
	    // 응답이 {"user":{...}} 이면 user를, 아니면 루트를 그대로 사용
	    JSONObject u = (JSONObject)(root.containsKey("user") ? root.get("user") : root);

	    User user = new User();
	    user.setUserId(str(u.get("userId")));
	    user.setUserName(str(u.get("userName")));
	    user.setAge(intg(u.get("age")));

	    System.out.println("파싱 결과(사용자) : userId=" + user.getUserId()
	            + ", userName=" + user.getUserName()
	            + ", age=" + user.getAge());

	    client.close();
	}

	
	//================================================================//
	// 1.2 Http Protocol GET Request : JsonSimple + CodeHaus(Jackson1) 사용
	public static void ReqeustHttpGet_UseCodeHaus() throws Exception {
	    String url = "http://127.0.0.1:8080/Spring13/app/userAPI/getUserMore/user01?name=" + enc("홍길동") + "&age=10";

	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader("Accept", "application/json");
	    httpGet.setHeader("Content-Type", "application/json; charset=UTF-8");

	    HttpResponse response = client.execute(httpGet);
	    String body = EntityUtils.toString(response.getEntity(), "UTF-8");

	    System.out.println("\n[GET/getUserMore] 요청 시작");
	    System.out.println("요청 URL : " + url);
	    System.out.println("서버 응답(JSON) : " + body);

	    // 예: {"user": {...}, "message":"..."}
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(body, new TypeReference<Map<String,Object>>(){});

	    User user = null;
	    if (map.get("user") != null) {
	        String userJson = mapper.writeValueAsString(map.get("user"));
	        user = mapper.readValue(userJson, User.class);
	    }

	    System.out.println("파싱 결과(사용자) : " + user);
	    System.out.println("부가 메시지 : " + map.get("message"));

	    client.close();
	}

	
	//================================================================//
	// 2.1 Http Protocol POST Request : JSON Body 전달 / JsonSimple 사용
	public static void ReqeustHttpPostData_UseJsonSimple() throws Exception {
	    String url = "http://127.0.0.1:8080/Spring13/app/userAPI/getUser";

	    // 요청 바디 JSON 생성(json-simple)
	    JSONObject sendObj = new JSONObject();
	    sendObj.put("userId", "user01");
	    sendObj.put("userName", "홍길동");
	    sendObj.put("age", 10);

	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(url);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
	    httpPost.setEntity(new StringEntity(sendObj.toJSONString(), "UTF-8"));

	    HttpResponse response = client.execute(httpPost);
	    String body = EntityUtils.toString(response.getEntity(), "UTF-8");

	    System.out.println("\n[POST/getUser] 요청 시작");
	    System.out.println("요청 URL : " + url);
	    System.out.println("전송 바디(JSON) : " + sendObj.toJSONString());
	    System.out.println("서버 응답(JSON) : " + body);

	    // 응답 JSON 파싱
	    JSONParser parser = new JSONParser();
	    JSONObject root = (JSONObject) parser.parse(body);
	    JSONObject u = (JSONObject)(root.containsKey("user") ? root.get("user") : root);

	    User user = new User();
	    user.setUserId(str(u.get("userId")));
	    user.setUserName(str(u.get("userName")));
	    user.setAge(intg(u.get("age")));

	    System.out.println("파싱 결과(사용자) : userId=" + user.getUserId()
	            + ", userName=" + user.getUserName()
	            + ", age=" + user.getAge());

	    client.close();
	}

	
	//================================================================//
	// 2.2 Http Protocol POST Request : JSON Body 전달 / JsonSimple + CodeHaus(Jackson1) 사용
	public static void ReqeustHttpPostData_UseCodeHaus() throws Exception {
	    String url = "http://127.0.0.1:8080/Spring13/app/userAPI/getUserMore/user01";

	    // 바디 JSON 생성
	    String bodyJson = "{\"userId\":\"user01\",\"userName\":\"홍길동\",\"age\":10}";

	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(url);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
	    httpPost.setEntity(new StringEntity(bodyJson, "UTF-8"));

	    HttpResponse response = client.execute(httpPost);
	    String body = EntityUtils.toString(response.getEntity(), "UTF-8");

	    System.out.println("\n[POST/getUserMore] 요청 시작");
	    System.out.println("요청 URL : " + url);
	    System.out.println("전송 바디(JSON) : " + bodyJson);
	    System.out.println("서버 응답(JSON) : " + body);

	    // 예: {"user": {...}, "message":"ok"}
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(body, new TypeReference<Map<String,Object>>(){});

	    User user = null;
	    if (map.get("user") != null) {
	        String userJson = mapper.writeValueAsString(map.get("user"));
	        user = mapper.readValue(userJson, User.class);
	    }

	    System.out.println("파싱 결과(사용자) : " + user);
	    System.out.println("부가 메시지 : " + map.get("message"));

	    client.close();
	}

	
	//================================================================//
	// util
	private static String enc(String s) throws Exception {
	    return java.net.URLEncoder.encode(s, "UTF-8");
	}
	private static String str(Object v) {
	    return v == null ? null : String.valueOf(v);
	}
	private static Integer intg(Object v) {
	    if (v == null) return null;
	    if (v instanceof Number) return ((Number) v).intValue();
	    try { return Integer.parseInt(String.valueOf(v)); } catch (Exception e) { return null; }
	}
}
