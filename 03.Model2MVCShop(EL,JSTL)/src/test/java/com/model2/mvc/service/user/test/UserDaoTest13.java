package com.model2.mvc.service.user.test;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.dao.UserDao;

/*
 *	UserDao 를 단위 테스트 하는 클래스
 */
 
public class UserDaoTest13 {

	public static void main(String[] args) throws Exception {
		
		// [핵심 1] 스프링 설정 파일을 읽어서 스프링 컨테이너(공장)를 만듦!
		ApplicationContext context =
			new ClassPathXmlApplicationContext(
				new String[] { "/config/commonservice.xml", // MyBatis + DB 연결 정보
							   "/config/userservice.xml" }   // 우리가 만들 DAO, Service 부품 정보
			);
		
		// [핵심 2] 스프링 공장에게 "userDaoImpl"이라는 이름의 완제품(Bean)을 달라고 요청!
		// 이제 우리가 new로 만들 필요가 없어!
		UserDao userDao = (UserDao) context.getBean("userDaoImpl");

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
		
		// 4. getUserList Test
		System.out.println("\n:: 4. getUserList 테스트 (이름으로 검색) ::");
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("1"); // 1: 이름
		search.setSearchKeyword("SCOTT");
		
		List<User> userList = userDao.getUserList(search);
		System.out.println("검색된 회원 목록:");
		for(User u : userList) {
			System.out.println(u);
		}
	}
}