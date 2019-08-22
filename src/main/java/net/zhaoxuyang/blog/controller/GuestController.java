package net.zhaoxuyang.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.zhaoxuyang.blog.exception.BlogException;
import static net.zhaoxuyang.blog.exception.BlogException.Type.UserLoginFail;
import net.zhaoxuyang.blog.model.User;
import net.zhaoxuyang.blog.service.ArticleService;
import net.zhaoxuyang.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zhaoxuyang
 */
@Controller
public class GuestController {

    @Autowired
    UserService userService;

    @RequestMapping("")
    public String userHome() {
        return "UserHome.html";
    }

    @GetMapping("login")
    public String loginForm() {
        return "Login.html";
    }

    @GetMapping("category.html")
    public String category() {
        return "category.html";
    }

    @GetMapping("detail.html")
    public String detail() {
        return "detail.html";
    }

    @GetMapping("index.html")
    public String home() {
        return "UserHome.html";
    }

    @PostMapping("login")
    public String loginAction(User loginUser, HttpSession session)
            throws BlogException {
        User user = userService.login(loginUser);
        if (user == null) {
            throw new BlogException(UserLoginFail, "账号密码不匹配!");
        } else {
            session.setAttribute("user", user);
            return "redirect:/user/conf";
        }
    }

    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "rss.xml", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    @ResponseBody
    public String rssXml(HttpServletRequest request) {
        //查出公开可见的前100篇文章转换成XML
        return articleService.createRssXml(request);
    }

}
