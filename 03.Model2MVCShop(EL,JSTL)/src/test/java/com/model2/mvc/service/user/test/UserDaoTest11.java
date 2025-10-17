package com.model2.mvc.service.user.test;

import org.apache.ibatis.session.SqlSession;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.dao.UserDao;
import com.model2.mvc.service.user.dao.UserDaoImpl;
import java.util.List;

public class UserDaoTest11 {

    public static void main(String[] args) throws Exception {
        
        SqlSession sqlSession = SqlSessionFactoryBean.getSqlSession();
        UserDao userDao = new UserDaoImpl(sqlSession);

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
        userDao.insertUser(user);
        
        // 2. findUser Test
        System.out.println("\n:: 2. findUser 테스트 ::");
        User foundUser = userDao.findUser("testUserId");
        System.out.println("찾은 회원 정보: " + foundUser);
        
        // 3. updateUser Test
        System.out.println("\n:: 3. updateUser 테스트 ::");
        foundUser.setUserName("수정된유저");
        userDao.updateUser(foundUser);
        System.out.println("수정된 회원 정보: " + userDao.findUser("testUserId"));
        
        // 4. getUserList & getTotalCount Test
        System.out.println("\n:: 4. getUserList 테스트 (이름으로 검색) ::");
        Search search = new Search();
        search.setCurrentPage(1);
        search.setPageSize(3);
        search.setSearchCondition("1"); // 1: 이름
        search.setSearchKeyword("SCOTT");
        
        int totalCount = userDao.getTotalCount(search);
        System.out.println("SCOTT 이름으로 검색된 총 회원 수: " + totalCount);
        
        List<User> userList = userDao.getUserList(search);
        System.out.println("검색된 회원 목록:");
        for(User u : userList) {
            System.out.println(u);
        }
        
        // 테스트가 끝나면 commit 또는 rollback을 해주는 게 좋아!
        // 여기서는 데이터를 남기기 위해 commit!
        sqlSession.commit();
        
        sqlSession.close();
    }
}