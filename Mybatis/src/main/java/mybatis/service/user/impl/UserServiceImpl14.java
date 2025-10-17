package mybatis.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mybatis.service.domain.Search;
import mybatis.service.domain.User;
import mybatis.service.user.UserDao;
import mybatis.service.user.UserService;

@Service("userServiceImpl14")
public class UserServiceImpl14 implements UserService {
	
	@Autowired
	@Qualifier("userDaoImpl14")
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		System.out.println(":: " + getClass().getName() + ".setUserDao() Call.....");
		this.userDao = userDao;
	}

	public UserServiceImpl14() {
		System.out.println(":: " + getClass().getName() + " 디폴트 생성자 call....");
	}

	@Override
	public int addUser(User user) throws Exception {
		return userDao.addUser(user);
	}

	@Override
	public User getUser(String userId) throws Exception {
		return userDao.getUser(userId);
	}
	
	@Override
	public int updateUser(User user) throws Exception {
		return userDao.updateUser(user);
	}
	
	@Override
	public int removeUser(String userId) throws Exception {
		return userDao.removeUser(userId);
	}

	@Override
	public List<User> getUserList(Search search) throws Exception {
		return userDao.getUserList(search);
	}


	public int[] addTwoUsers(User user1, User user2) throws Exception {
		System.out.println("SERVICE14 addTwoUsers 시작");
		System.out.println("SERVICE14 user1=" + user1);
		System.out.println("SERVICE14 user2=" + user2);

		int r1 = userDao.addUser(user1);
		System.out.println("SERVICE14 첫번째 insert 결과=" + r1);

		int r2 = userDao.addUser(user2);
		System.out.println("SERVICE14 두번째 insert 결과=" + r2);

		System.out.println("SERVICE14 결과 배열 r1=" + r1 + ", r2=" + r2);
		System.out.println("SERVICE14 addTwoUsers 종료");
		return new int[]{ r1, r2 };
	}
}
