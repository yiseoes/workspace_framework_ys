package json.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import spring.domain.User;

public class JSONObjectMapperTestApp {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        User user01 = new User("user01", "홍길동", "1111", null, 10);
        User user02 = new User("user02", "홍길동", "2222", null, 20);

        // ==============================================================
        // 1. Domain Object -> JSON Value(String)
        // ==============================================================
        String userJson = mapper.writeValueAsString(user01);
        System.out.println("1. Domain Object -> JSON Value(String)");
        System.out.println("   " + userJson);

        // (1-1) JSON Value -> Domain Object 변환 및 값 추출
        User userFromJson = mapper.readValue(userJson, User.class);
        System.out.println("1-1) JSON -> Domain Object 변환 및 값 추출");
        System.out.println("   userId=" + userFromJson.getUserId() + 
                           ", userName=" + userFromJson.getUserName());

        // (1-2) JSON Value -> JSON Object 사용 및 값 추출
        JsonNode userNode = mapper.readTree(userJson);
        System.out.println("1-2) JSON -> JsonNode 사용 및 값 추출");
        System.out.println("   userId=" + userNode.get("userId").asText() + 
                           ", grade=" + userNode.get("grade").asInt());
        
        
        
        System.out.println("");
        System.out.println("");

        // ==============================================================
        // 2. List<User> -> JSON Value(String)
        // ==============================================================
        List<User> userList = new ArrayList<User>();
        userList.add(user01);
        userList.add(user02);

        String listJson = mapper.writeValueAsString(userList);
        System.out.println("2. List<User> -> JSON Value(String)");
        System.out.println("   " + listJson);

        // (2-1) JSON Value -> List<User> 변환 및 값 추출
        List<User> listFromJson = mapper.readValue(listJson, new TypeReference<List<User>>() {});
        System.out.println("2-1) JSON -> List<User> 변환 및 값 추출");
        for (User u : listFromJson) {
            System.out.println("   " + u);
        }

        // (2-2) JSON Value -> JSON Object 사용 및 값 추출
        JsonNode listNode = mapper.readTree(listJson);
        System.out.println("2-2) JSON -> JsonNode(Array)사용 및 값 추출");
        for (JsonNode n : listNode) {
            System.out.println("   userId=" + n.get("userId").asText() + 
                               ", grade=" + n.get("grade").asInt());
        }
        
        System.out.println("");
        System.out.println("");

        // ==============================================================
        // 3. Map<User> -> JSON Value(String)
        // ==============================================================
        Map<String, User> userMap = new HashMap<String, User>();
        userMap.put("user01", user01);
        userMap.put("user02", user02);

        String mapJson = mapper.writeValueAsString(userMap);
        System.out.println("3. Map<User> -> JSON Value(String)");
        System.out.println("   " + mapJson);

        // (3-1) JSON Value -> Map<User> 변환 및 값 추출
        Map<String, User> mapFromJson = mapper.readValue(mapJson, new TypeReference<Map<String, User>>() {});
        System.out.println("3-1) JSON -> Map<User>변환 및 값 추출");
        System.out.println("   user01=" + mapFromJson.get("user01"));
        System.out.println("   user02=" + mapFromJson.get("user02"));

        // (3-2) JSON Value -> JSON Object 사용 및 값 추출
        JsonNode mapNode = mapper.readTree(mapJson);
        System.out.println("3-2) JSON -> JsonNode(Object)사용 및 값 추출");
        System.out.println("   user01.name=" + mapNode.get("user01").get("userName").asText());
        System.out.println("   user02.name=" + mapNode.get("user02").get("userName").asText());
    }
}
