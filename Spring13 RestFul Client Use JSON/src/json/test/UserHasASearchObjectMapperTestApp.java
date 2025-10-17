package json.test;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import spring.domain.Search;
import spring.domain.UserHasASearch;

public class UserHasASearchObjectMapperTestApp {

    // 오브젝트 맵퍼 객체생성 (static만 유지)
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        // 0) 테스트 데이터 : user01 + Search(Has-A)
        UserHasASearch user = new UserHasASearch("user01", "홍길동", "1111", null, 10);
        Search search = new Search();
        search.setSearchCondition("이름검색"); // Has-A 관계
        user.setSearch(search);

        // 1. Domain Object -> JSON Value(String) 로 변환
        String json = mapper.writeValueAsString(user);
        System.out.println("1. Domain Object -> JSON Value(String) 로 변환");
        System.out.println("   " + json);

        // (1-1) JSON Value -> Domain Object 변환 및 값 추출
        UserHasASearch fromJson = mapper.readValue(json, UserHasASearch.class);
        System.out.println("(1-1) JSON Value -> Domain Object 변환 및 값 추출");
        System.out.println("   " + fromJson.toString()); // toString 오버라이딩 결과 확인

        // (1-2) JSON Value -> JSON Object 사용 및 값 추출 (JSON-simple 사용)
        System.out.println("(1-2) JSON Value -> JSON Object 사용 및 값 추출");
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(json);

        String p_userId   = (String) root.get("userId");
        String p_userName = (String) root.get("userName");
        Number p_grade    = (Number) root.get("grade");
        JSONObject searchObj = (JSONObject) root.get("search");

        System.out.println("   userId=" + p_userId);
        System.out.println("   userName=" + p_userName);
        System.out.println("   grade=" + (p_grade==null?null:p_grade.intValue()));
        System.out.println("   search=" + (searchObj==null?null:searchObj.toJSONString()));

        // ==== (검색) userId 기반 간단 검색 시뮬레이션 ====
        //   - 수업/내부 테스트용 : userId가 'user01'이면 '검색 성공' 표시
        if ("user01".equals(p_userId)) {
            System.out.println(">>> [검색] userId='user01' : 검색 성공");
            // 필요시 검색 결과 객체 출력
            System.out.println(">>> [검색결과 객체] " + fromJson.toString());
        } else {
            System.out.println(">>> [검색] userId='user01' : 검색 실패");
        }
    }
}
