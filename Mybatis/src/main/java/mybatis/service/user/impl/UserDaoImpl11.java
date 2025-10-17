package mybatis.service.user.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.service.domain.Search;
import mybatis.service.domain.User;
import mybatis.service.user.UserDao;

//[설명] UserDao 인터페이스의 규칙을 실제로 구현하는 클래스
public class UserDaoImpl11 implements UserDao{
	
	// [핵심] MyBatis의 핵심 기능을 가진 SqlSession을 필드로 선언
	private SqlSession sqlSession;
	
	// [주입] 외부(테스트 클래스)에서 SqlSession 객체를 넣어줄 수 있도록 setter 메소드를 만듦
	public void setSqlSession(SqlSession sqlSession) {
		System.out.println(":: "+getClass().getName()+".setSqlSession() Call.....");
		this.sqlSession = sqlSession;
	}

	// [기본] 생성자
	public UserDaoImpl11() {
		System.out.println(":: "+getClass().getName()+" 디폴트 생성자 call....");
	}

	// [구현] addUser 기능
	// - 받은 user 정보를 'UserMapper11.addUser' 쿼리로 전달해서 실행
	@Override
	public int addUser(User user) throws Exception {
		return sqlSession.insert("UserMapper10.addUser", user);
	}

	// [구현] getUser 기능
	// - 받은 userId를 'UserMapper11.getUser' 쿼리로 전달해서 실행
	@Override
	public User getUser(String userId) throws Exception {
		return sqlSession.selectOne("UserMapper10.getUser", userId);
	}
	
	// [구현] updateUser 기능
	// - 받은 user 정보를 'UserMapper11.updateUser' 쿼리로 전달해서 실행
	@Override
	public int updateUser(User user) throws Exception {
		return sqlSession.update("UserMapper10.updateUser", user);
	}
	
	// [구현] removeUser 기능
	// - 받은 userId를 'UserMapper11.removeUser' 쿼리로 전달해서 실행
	@Override
	public int removeUser(String userId) throws Exception {
		return sqlSession.delete("UserMapper10.removeUser", userId);
	}

	// [구현] getUserList 기능
	// - 받은 search 정보를 'UserMapper11.getUserList' 쿼리로 전달해서 실행
	@Override
	public List<User> getUserList(Search search) throws Exception {
		return sqlSession.selectList("UserMapper10.getUserList", search);
	}

}