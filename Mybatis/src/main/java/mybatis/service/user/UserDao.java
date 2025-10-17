package mybatis.service.user;

import java.util.List;
import mybatis.service.domain.Search;
import mybatis.service.domain.User;

//[설명] User 데이터를 DB에 접근하여 처리하는 모든 기능의 '규칙'을 정의하는 인터페이스
public interface UserDao {
	
	// 회원 추가
	public int addUser(User user) throws Exception ;
	
	// 회원 정보 조회 (1명)
	public User getUser(String userId) throws Exception ;
	
	// 회원 정보 수정
	public int updateUser(User user) throws Exception ;
	
	// 회원 삭제
	public int removeUser(String userId) throws Exception ;
	
	// 회원 목록 조회 (검색 기능 포함)
	public List<User> getUserList(Search search) throws Exception ;

}