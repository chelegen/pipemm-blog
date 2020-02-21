package hello.controller;

import hello.Utils.AssertUtils;
import hello.entity.Blog;
import hello.entity.BlogResult;
import hello.entity.Result;
import hello.entity.User;
import hello.service.AuthService;
import hello.service.BlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class BlogController {
    private final BlogService blogService;
    private final AuthService authService;

    @Inject
    public BlogController(BlogService blogService, AuthService authService) {
        this.blogService = blogService;
        this.authService = authService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public Result getBlogs(@RequestParam("page") Integer page,
                           @RequestParam(value = "userId", required = false) Integer userId) {
        if (page == null || page < 0) {
            page = 1;
        }
        return blogService.getBlogs(page, 10, userId);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public Result<Blog> getBlog(@PathVariable("blogId") int blogId) {
        return blogService.getBlogById(blogId);
    }

    @PostMapping("/blog")
    @ResponseBody
    public Result newBlog(@RequestBody Map<String, String> params) {
        try {
            return authService.getCurrentUser().map(user -> blogService.insertBlog(fromParam(params, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public Result updateBlog(@PathVariable("blogId") int blogId,
                             @RequestBody Map<String, String> params) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlog(blogId, (fromParam(params, user))))
                    .orElse(BlogResult.failure("登录后才能操作"));

        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public Result deleteBlog(@PathVariable("blogId") int blogId) {
        try {
            return authService.getCurrentUser().map(user -> blogService.deleteBlog(user, blogId))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    private Blog fromParam(Map<String, String> params, User user) {
        Blog blog = new Blog();
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");

        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "标题错误");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 1000, "内容错误");

        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(description.length(), 10)) + "...";
        }

        blog.setTitle(title);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setUser(user);
        return blog;
    }
}
