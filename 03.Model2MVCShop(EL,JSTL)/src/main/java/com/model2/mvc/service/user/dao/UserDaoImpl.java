package com.model2.mvc.service.user.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;

public class UserDaoImpl implements UserDao {
	
	private SqlSession sqlSession;
	
	public UserDaoImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void insertUser(User user) throws Exception {
		sqlSession.insert("UserMapper.insertUser", user);
	}

	@Override
	public User findUser(String userId) throws Exception {
		return sqlSession.selectOne("UserMapper.findUser", userId);
	}
	
	@Override
	public List<User> getUserList(Search search) throws Exception {
		return sqlSession.selectList("UserMapper.getUserList", search);
	}
	
	@Override
	public void updateUser(User user) throws Exception {
		sqlSession.update("UserMapper.updateUser", user);
	}
	
	@Override
	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("UserMapper.getTotalCount", search);
	}
}