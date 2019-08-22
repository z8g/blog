package net.zhaoxuyang.blog.interceptor;

import net.zhaoxuyang.blog.exception.BlogException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static net.zhaoxuyang.blog.exception.BlogException.Type.UserNoPermission;
import net.zhaoxuyang.blog.model.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException, BlogException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("user");
        boolean isLogin = loginUser != null;
        
        if (!isLogin) {
            throw new BlogException(UserNoPermission,"请登录后再操作!");
            //response.sendError(403);
            //response.sendRedirect("http://"+request.getServerName()+":"+request.getServerPort()+"/admin/login");
        }
        return isLogin;
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

}
