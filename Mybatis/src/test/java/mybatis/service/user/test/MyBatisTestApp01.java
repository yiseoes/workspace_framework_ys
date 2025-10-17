package mybatis.service.user.test;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import mybatis.service.domain.User;

public class MyBatisTestApp01 {

    public static void main(String[] args) throws Exception {
        try (Reader reader = Resources.getResourceAsReader("sql/mybatis-config01.xml")) {
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
            try (SqlSession session = factory.openSession(true)) {

                // 1. 모든 유저 정보
                List<User> all = session.selectList("sql.UserMapper01.getUserList");
                System.out.println("[1] getUserList ===");
                for (User u : all) {
                    System.out.println(u);
                }

                // 2. userId 단건 조회
                User one = session.selectOne("sql.UserMapper01.getUser", "user01");
                System.out.println("\n[2] getUser ===");
                System.out.println(one);

                // 3. userId + password → 유저이름(String)
                User param1 = new User();
                param1.setUserId("user03");
                param1.setPassword("user03");
                String name1 = session.selectOne("sql.UserMapper01.findUserId", param1);
                System.out.println("\n[3] findUserId ===");
                System.out.println(name1);

                // 4. age → 유저이름(String)
                User param2 = new User();
                param2.setAge(20);
                List<String> names = session.selectList("sql.UserMapper01.getUserListAge", param2);
                System.out.println("\n[4] getUserListAge ===");
                for (String n : names) {
                    System.out.println(n);
                }
            }
        }
    }
}
