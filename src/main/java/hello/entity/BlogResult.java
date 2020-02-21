package hello.entity;

public class BlogResult extends Result<Blog> {
    private BlogResult(ResultStatus status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult(ResultStatus.FAIL, msg, null);
    }

    public static BlogResult success(String msg) {
        return success(msg, null);
    }

    public static BlogResult success(String msg, Blog blog) {
        return new BlogResult(ResultStatus.OK, msg, blog);
    }
}
