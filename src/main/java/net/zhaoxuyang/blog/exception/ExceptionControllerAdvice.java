package net.zhaoxuyang.blog.exception;

import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import net.zhaoxuyang.blog.model.ArticleAuth;
import net.zhaoxuyang.blog.model.Conf;
import net.zhaoxuyang.blog.model.User;
import net.zhaoxuyang.blog.service.ArticleService;
import net.zhaoxuyang.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常 controller 增强器
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值 绑定后请在后面注释作者
     *
     * @param model
     * @param session
     */
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        Map<String, Conf> confMap = userService.getConfMap();//配置信息

        ArticleAuth articleAuth = (ArticleAuth) session.getAttribute("articleAuth");
        int auth = articleAuth == null ? 0 : articleAuth.getValue();
        Set<String> tagSet = articleService.getTagSet(auth);//标签
        Map<String, Integer> categoryMap = articleService.getCategoryMap(auth);//分类
        Map<String, Integer> yearMonthMap = articleService.getYearMonthMap(auth);//归档

        model.addAttribute("isLogin", user != null);
        model.addAttribute("confMap", confMap);
        model.addAttribute("tagSet", tagSet);
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("yearMonthMap", yearMonthMap);
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BlogException.class)
    public ModelAndView errorHandler(BlogException ex) {
        ModelAndView result = new ModelAndView();
        result.addObject("exceptionTitle", ex.getType().getTitle());
        result.addObject("exceptionMessage", ex.getMessage());
        result.setViewName("Exception.html");
        return result;
    }

}
