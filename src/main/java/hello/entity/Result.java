package hello.entity;

public class Result {
    // json序列化结果一般来说取决于getter和setter方法，与字段没关系
    String status;
    String msg;
    boolean isLogin;
    Object data;

    public static Result failure(String message) {
        return new Result("fail", message, false);
    }

    public static Result success(String message, boolean isLogin, Object data) {
        return new Result("ok", message, isLogin, data);
    }

    public static Result success(String message, boolean isLogin) {
        return new Result("ok", message, isLogin);
    }

    private Result(String status, String msg, boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    private Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}