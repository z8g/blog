package net.zhaoxuyang.blog.interceptor;

import net.zhaoxuyang.blog.exception.BlogException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.zhaoxuyang.blog.model.ArticleAuth;
import net.zhaoxuyang.blog.model.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ArticleAuthInterceptor implements HandlerInterceptor {

    /**
     * 获取搜索的文章权限
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws java.io.IOException
     * @throws net.zhaoxuyang.blog.exception.BlogException
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException, BlogException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("user");
        session.setAttribute("articleAuth", getArticleAuth(loginUser));
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) {

    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {

    }

    private static ArticleAuth getArticleAuth(User loginUser) {
        return loginUser == null ? ArticleAuth.PUBLIC : ArticleAuth.PRIVATE;
    }

}
