package com.model2.mvc.service.user.test;

import org.apache.ibatis.session.SqlSession;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.dao.UserDao;
import com.model2.mvc.service.user.dao.UserDaoImpl;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import java.util.Map;
import java.util.List;

public class UserServiceTest12 {

    public static void main(String[] args) throws Exception {
        
        SqlSession sqlSession = SqlSessionFactoryBean.getSqlSession();
        
        // [핵심 1] DB 전문가(DAO)를 먼저 만들고
        UserDao userDao = new UserDaoImpl(sqlSession);
        
        // [핵심 2] 그 전문가를 매니저(Service)에게 전달해서 매니저를 만듦! (의존성 주입)
        UserService userService = new UserServiceImpl(userDao);

        //== Test User Instance 생성
        User user = new User();
        user.setUserId("testSvcUser");
        user.setUserName("서비스테스트");
        user.setPassword("1234");

        // 1. addUser Test
        System.out.println(":: 1. addUser 테스트 ::");
        userService.addUser(user);
        
        // 2. getUser Test
        System.out.println("\n:: 2. getUser 테스트 ::");
        User foundUser = userService.getUser("testSvcUser");
        System.out.println("찾은 회원 정보: " + foundUser);
        
        // 3. updateUser Test
        System.out.println("\n:: 3. updateUser 테스트 ::");
        foundUser.setUserName("서비스수정");
        userService.updateUser(foundUser);
        System.out.println("수정된 회원 정보: " + userService.getUser("testSvcUser"));
        
        // 4. getUserList Test
        System.out.println("\n:: 4. getUserList 테스트 ::");
        Search search = new Search();
        search.setCurrentPage(1);
        search.setPageSize(3);
        
        Map<String, Object> resultMap = userService.getUserList(search);
        List<User> userList = (List<User>) resultMap.get("list");
        int totalCount = (Integer) resultMap.get("totalCount");
        
        System.out.println("전체 회원 수: " + totalCount);
        System.out.println("현재 페이지 회원 목록:");
        for(User u : userList) {
            System.out.println(u);
        }
        
        sqlSession.commit();
        sqlSession.close();
    }
}