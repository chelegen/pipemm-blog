package hello.entity;

public class LoginResult extends Result<User> {
    boolean isLogin;

    private LoginResult(ResultStatus status, String msg, User data, boolean isLogin) {
        super(status, msg, data);
        this.isLogin = isLogin;
    }

    public static Result success(String msg, boolean isLogin) {
        return new LoginResult(ResultStatus.OK, msg, null, isLogin);
    }

    public static Result success(User user) {
        return success(null, user);
    }

    public static Result success(String msg, User user) {
        return new LoginResult(ResultStatus.OK, msg, user, true);
    }

    public static Result failure(String message) {
        return new LoginResult(ResultStatus.FAIL, message, null, false);
    }

    public boolean isLogin() {
        return isLogin;
    }
}
