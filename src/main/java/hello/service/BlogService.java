package hello.service;

import hello.dao.BlogDao;
import hello.entity.*;
import org.graalvm.compiler.core.common.SuppressFBWarnings;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    private BlogDao blogDao;
    private UserService userService;

    @Inject
    public BlogService(BlogDao blogDao, UserService userServicel) {
        this.blogDao = blogDao;
        this.userService = userServicel;
    }

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "我希望把这个错误忽略掉")
    public BlogListResult getBlogs(Integer page, Integer pageSize, Integer userId, boolean atIndex) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId, atIndex);

//            blogs.forEach(blog -> blog.setUser(userService.getUserById(blog.getUserId())));

            int count = blogDao.count(userId);

            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogListResult.success(blogs, count, page, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult getBlogById(int blogId) {
        try {
            return BlogResult.success("获取成功", blogDao.getBlogById(blogId));
        } catch (Exception e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    public Result insertBlog(Blog blog) {
        try {
            return BlogResult.success("创建成功", blogDao.insertBlog(blog));
        } catch (Exception e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    public BlogResult updateBlog(int blogId, Blog targetBlog) {
        Blog blogInDb = blogDao.getBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!Objects.equals(blogId, blogInDb.getId())) {
            return BlogResult.failure("无法修改别人的博客");
        }
        try {
            targetBlog.setId(blogId);
            return BlogResult.success("修改成功", blogDao.updateBlog(targetBlog));
        } catch (Exception e) {
            return BlogResult.failure(e.getMessage());
        }
    }


    public BlogResult deleteBlog(User user, int blogId) {
        Blog blogInDb = blogDao.getBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!Objects.equals(blogId, blogInDb.getId())) {
            return BlogResult.failure("无法删除别人的博客");
        }
        try {
            blogDao.deleteBlog(blogId);
            return BlogResult.success("删除成功");
        } catch (Exception e) {
            return BlogResult.failure(e.getMessage());

        }
    }
}
