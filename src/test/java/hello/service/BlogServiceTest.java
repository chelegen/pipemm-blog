package hello.service;

import hello.dao.BlogDao;
import hello.entity.Blog;
import hello.entity.BlogResult;
import hello.entity.Result;
import hello.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogDao blogDao;
    @InjectMocks
    BlogService blogService;

    @Test
    public void getBlogsFromDb() {
        blogService.getBlogs(1, 10, null, true);
        verify(blogDao).getBlogs(1, 10, null, true);
    }

    @Test
    public void returnOkWhenGetAtIndexBlogs() {
        Result result = blogService.getBlogs(1, 10, null, true);
        Assertions.assertEquals("ok", result.getStatus());
    }

    @Test
    public void returnFailureWhenBlogNotFound() {
        when(blogDao.getBlogById(1)).thenReturn(null);

        BlogResult result = blogService.deleteBlog(mock(User.class), 1);

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("博客不存在", result.getMsg());
    }

    @Test
    public void returnFailureWhenBlogUserIdNotMatch() {
        User blogAuthor = new User(123, "blogAuthor", "");
        User operator = new User(456, "operator", "");

        Blog targetBlog = new Blog();
        targetBlog.setId(1);
        targetBlog.setUser(operator);

        Blog blogInDb = new Blog();
        blogInDb.setUser(blogAuthor);

        when(blogDao.getBlogById(1)).thenReturn(blogInDb);
        BlogResult result = blogService.updateBlog(1, targetBlog);

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("无法修改别人的博客", result.getMsg());
    }
}
