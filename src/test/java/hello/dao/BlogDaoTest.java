package hello.dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class BlogDaoTest {
    private SqlSession mockSession = Mockito.mock(SqlSession.class);
    private BlogDao blogDao = new BlogDao(mockSession);

//    @BeforeEach
//    public void setUp() {
// 如果sqlSession是字段注入的话:
//        SqlSession session = Mockito.mock(SqlSession.class);
//        final Field sqlSession = BlogDao.class.getField("sqlSession");
//        sqlSession.setAccessible(true);
//        sqlSession.set(blogDao, session);
//    }

    @Test
    public void testGetBlogs() {
        // 当我传递 page=2 pageSize=10 user_id = 1
        // 数据库得到的参数就是
        // user_id = 1
        // offset = 10
        // limit = 10
        blogDao.getBlogs(2, 10, 1);
        Map<String, Object> expectedParam = new HashMap<>();
        expectedParam.put("user_id", 1);
        expectedParam.put("offset", 10);
        expectedParam.put("limit", 10);
        Mockito.verify(mockSession).selectList("selectBlog", expectedParam);

    }

}