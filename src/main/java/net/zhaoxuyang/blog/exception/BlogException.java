package net.zhaoxuyang.blog.exception;

/**
 * 博客 - 异常
 *
 * @author zhaoxuyang
 */
public class BlogException extends Exception {

    private Type type;

    public BlogException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        ArticleNoPermission("[文章无权限]异常"),
        ArticleNotFound("[文章不存在]异常"),
        Article("[文章]异常"),
        UserNoPermission("[用户无权限]异常"),
        UserNotFound("[用户不存在]异常"),
        UserLoginFail("[用户登录失败]异常"),
        User("[用户]异常");

        String title;

        Type(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
