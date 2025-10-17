package com.model2.mvc.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.dao.UserDao;

public class UserServiceImpl implements UserService {

	// [핵심!] DB 전문가인 UserDao를 부품으로 가지고 있어!
	private UserDao userDao;
	
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void addUser(User user) throws Exception {
		// 특별한 비즈니스 로직이 없으므로, 그대로 DAO에게 일을 넘겨!
		userDao.insertUser(user);
	}

	@Override
	public User loginUser(User user) throws Exception {
		// ID로 일단 유저 정보를 찾아와서
		User dbUser = userDao.findUser(user.getUserId());

		// 비밀번호가 일치하지 않으면 에러를 발생시키는 '비즈니스 로직'을 수행!
		if (dbUser == null || !dbUser.getPassword().equals(user.getPassword())) {
			throw new Exception("아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		return dbUser;
	}

	@Override
	public User getUser(String userId) throws Exception {
		return userDao.findUser(userId);
	}

	@Override
	public Map<String, Object> getUserList(Search search) throws Exception {
		// DAO를 통해 목록과 전체 개수를 각각 가져와서
		List<User> list = userDao.getUserList(search);
		int totalCount = userDao.getTotalCount(search);
		
		// Map에 예쁘게 포장해서 돌려주는 '비즈니스 로직'을 수행!
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", totalCount);
		
		return map;
	}

	@Override
	public void updateUser(User user) throws Exception {
		userDao.updateUser(user);
	}

	@Override
	public boolean checkDuplication(String userId) throws Exception {
		// ID로 유저를 찾아보고,
		User user = userDao.findUser(userId);
		
		// 유저가 존재하면 중복(false), 존재하지 않으면 사용 가능(true)이라고 판단하는 '비즈니스 로직'을 수행!
		if(user != null) {
			return false; // 중복
		}
		return true; // 사용 가능
	}
}