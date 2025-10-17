package mybatis.service.user.impl;

import java.util.List;

import mybatis.service.domain.Search;
import mybatis.service.domain.User;
import mybatis.service.user.UserDao; // [핵심!] UserDaoImpl11이 아닌 UserDao 인터페이스를 사용!
import mybatis.service.user.UserService;

// [설명] UserService 인터페이스의 규칙을 실제로 구현하는 클래스
public class UserServiceImpl12 implements UserService {
	
	// [핵심!] UserDaoImpl11 클래스가 아닌, UserDao '인터페이스'를 필드로 선언 (느슨한 결합!)
	private UserDao userDao;
	
	// [주입] 외부(테스트 클래스)에서 UserDao를 구현한 객체(UserDaoImpl11)를 넣어줄 수 있도록 setter 메소드를 만듦
	public void setUserDao(UserDao userDao) {
		System.out.println(":: "+getClass().getName()+".setUserDao() Call.....");
		this.userDao = userDao;
	}

	// [기본] 생성자
	public UserServiceImpl12() {
		System.out.println(":: "+getClass().getName()+" 디폴트 생성자 call....");
	}

	// [구현] addUser 기능
	// - 실제 DB 작업은 전문가인 userDao에게 그대로 위임(delegate)한다.
	@Override
	public int addUser(User user) throws Exception {
		return userDao.addUser(user);
	}

	// [구현] getUser 기능
	@Override
	public User getUser(String userId) throws Exception {
		return userDao.getUser(userId);
	}
	
	// [구현] updateUser 기능
	@Override
	public int updateUser(User user) throws Exception {
		return userDao.updateUser(user);
	}
	
	// [구현] removeUser 기능
	@Override
	public int removeUser(String userId) throws Exception {
		return userDao.removeUser(userId);
	}

	// [구현] getUserList 기능
	@Override
	public List<User> getUserList(Search search) throws Exception {
		return userDao.getUserList(search);
	}

}