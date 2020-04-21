package hello.dao;

import hello.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private Map<String, Object> asMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId, boolean atIndex) {
        Map<String, Object> parameters = asMap(
                "user_id", userId,
                "offset", (page - 1) * pageSize,
                "limit", pageSize,
                "atIndex", atIndex
        );
        return sqlSession.selectList("selectBlog", parameters);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlogs", asMap("user_id", userId));
    }

    public Blog getBlogById(int id) {
        return sqlSession.selectOne("getBlogById", asMap("id", id));
    }

    public Blog insertBlog(Blog blog) {
        sqlSession.insert("insertBlog", blog);
        return getBlogById(blog.getId());
    }

    public Blog updateBlog(Blog targetBlog) {
        sqlSession.update("updateBlog", targetBlog);
        return getBlogById(targetBlog.getId());
    }

    public void deleteBlog(int blogId) {
        sqlSession.delete("deleteBlog", blogId);
    }
}
