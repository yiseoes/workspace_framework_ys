package com.model2.mvc.service.user.test;

import org.apache.ibatis.session.SqlSession;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import java.util.List;

// 우리가 이전에 쓰던 SqlSessionFactoryBean을 테스트 패키지에 가져다 놓거나,
// 경로를 맞춰서 import 해야 해!
// 예: import mybatis.service.user.test.SqlSessionFactoryBean;

public class UserMapperTest10 {

    public static void main(String[] args) throws Exception {
        
        // SqlSessionFactoryBean은 우리가 연습 때 쓰던 클래스야!
        // 이서 프로젝트의 테스트 패키지로 복사해오면 돼!
        SqlSession sqlSession = SqlSessionFactoryBean.getSqlSession();

        //== Test User Instance 생성
        User user = new User();
        user.setUserId("testUserId");
        user.setUserName("테스트유저");
        user.setPassword("1234");
        user.setAddr("서울");
        user.setEmail("test@test.com");
        user.setPhone("010-1234-5678");

        // 1. insertUser Test
        System.out.println(":: 1. insertUser 테스트 ::");
        sqlSession.insert("UserMapper.insertUser", user);
        
        // 2. findUser Test
        System.out.println("\n:: 2. findUser 테스트 ::");
        User foundUser = sqlSession.selectOne("UserMapper.findUser", "testUserId");
        System.out.println("찾은 회원 정보: " + foundUser);
        
        // 3. updateUser Test
        System.out.println("\n:: 3. updateUser 테스트 ::");
        foundUser.setUserName("수정된유저");
        sqlSession.update("UserMapper.updateUser", foundUser);
        System.out.println("수정된 회원 정보: " + sqlSession.selectOne("UserMapper.findUser", "testUserId"));
        
        // 4. getUserList & getTotalCount Test
        System.out.println("\n:: 4. getUserList 테스트 (이름으로 검색) ::");
        Search search = new Search();
        search.setCurrentPage(1);
        search.setPageSize(3);
        search.setSearchCondition("1"); // 1: 이름
        search.setSearchKeyword("SCOTT");
        
        int totalCount = sqlSession.selectOne("UserMapper.getTotalCount", search);
        System.out.println("SCOTT 이름으로 검색된 총 회원 수: " + totalCount);
        
        List<User> userList = sqlSession.selectList("UserMapper.getUserList", search);
        System.out.println("검색된 회원 목록:");
        for(User u : userList) {
            System.out.println(u);
        }
        
        sqlSession.close();
    }
}