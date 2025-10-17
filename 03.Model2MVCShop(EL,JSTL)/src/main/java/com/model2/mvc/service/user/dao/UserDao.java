package com.model2.mvc.service.user.dao;

import java.util.List;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;

public interface UserDao {
	
	public void insertUser(User user) throws Exception ;
	
	public User findUser(String userId) throws Exception ;
	
	public List<User> getUserList(Search search) throws Exception ;
	
	public void updateUser(User user) throws Exception ;
	
	// getTotalCount는 페이징 처리를 위해 목록 조회 기능에 꼭 필요한 짝꿍이야!
	public int getTotalCount(Search search) throws Exception ;

}