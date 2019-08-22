package net.zhaoxuyang.blog.controller;

import com.github.pagehelper.PageInfo;
import javax.servlet.http.HttpSession;
import net.zhaoxuyang.blog.exception.BlogException;
import net.zhaoxuyang.blog.model.Article;
import net.zhaoxuyang.blog.model.ArticleAuth;
import net.zhaoxuyang.blog.model.Conf;
import net.zhaoxuyang.blog.service.ArticleService;
import net.zhaoxuyang.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zhaoxuyang
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;

    @RequestMapping("logout")
    public String logoutAction(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("conf")
    public String confMap() {
        return "UserConf.html";
    }

    @PostMapping("conf")
    public String confUpdate(Conf conf, Model model) {
        userService.setConf(conf);
        return "redirect:/user/conf";
    }

    @RequestMapping("article/{pageCurr}")
    public String articleList(
            @PathVariable Integer pageCurr,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        if (pageCurr == null) {
            pageCurr = 1;
        }
        int auth = getArticleAuth(session);//查询文章的权限
        PageInfo pageInfo = articleService.list(pageCurr, pageSize, auth);
        model.addAttribute("pageInfo", pageInfo);
        return "UserArticle.html";
    }

    @RequestMapping(value = {"article"}, produces = "text/html; charset=utf-8")
    public String articleList1(HttpSession session) {
        return "redirect:/user/article/1";//返回文章列表第1页
    }

    @GetMapping("article/update/{articleId}")
    public String articleUpdateForm(
            @PathVariable Integer articleId,
            HttpSession session,
            Model model
    ) throws BlogException {
        int auth = getArticleAuth(session);//查询文章的权限
        Article article = articleService.get(auth, articleId);
        if (article == null) {
            throw new BlogException(BlogException.Type.ArticleNotFound,
                    String.format("文章ID为%d的文章不存在!", articleId));
        }
        model.addAttribute("article", article);
        return "UserArticleEdit.html";
    }

    @PostMapping("article/update/{articleId}")
    @ResponseBody
    public Object articleUpdateAction(
            @PathVariable Integer articleId,
            Article article
    ) {
        article.setId(articleId);
        return articleService.update(article);
    }

    @GetMapping("article/create")
    public String articleCreateForm(Model model) {
        Article article = new Article();
        model.addAttribute("article", article);
        return "UserArticleEdit.html";
    }

    @PostMapping("article/create")
    public String articleCreateAction(Article article, Model model) {
        articleService.insert(article);
        model.addAttribute("article", article);
        return "UserArticleEdit.html";
    }

    @RequestMapping("article/delete/{articleId}")
    public String articleDelete(@PathVariable Integer articleId) {
        articleService.delete(articleId);
        return "redirect:/user/article";
    }

    private int getArticleAuth(HttpSession session) {
        ArticleAuth articleAuth = (ArticleAuth) session.getAttribute("articleAuth");
        return articleAuth == null ? 0 : articleAuth.getValue();//查询文章的权限
    }

}
