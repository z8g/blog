package net.zhaoxuyang.blog.controller;

import net.zhaoxuyang.blog.model.ArticleAuth;
import com.github.pagehelper.PageInfo;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import static java.net.URLEncoder.encode;
import javax.servlet.http.HttpSession;
import net.zhaoxuyang.blog.exception.BlogException;
import static net.zhaoxuyang.blog.exception.BlogException.Type.ArticleNotFound;
import net.zhaoxuyang.blog.model.Article;
import net.zhaoxuyang.blog.service.ArticleService;
import net.zhaoxuyang.blog.service.UserService;
import net.zhaoxuyang.blog.util.DatetimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zhaoxuyang
 */
@Controller
@RequestMapping("a")//article
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;

    final static Logger LOG = LoggerFactory
            .getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/{articleId}")
    public String details(
            @PathVariable Integer articleId,
            Model model,
            HttpSession session
    ) throws BlogException {

        int auth = getArticleAuth(session);
        Article article = articleService.get(auth, articleId);//根据权限获取文章

        if (article == null) {
            //文章不存在的情况
            String msg = String.format("ID为%d的文章不存在！", articleId);
            throw new BlogException(ArticleNotFound, msg);
        } else {
            //文章存在的情况
            model.addAttribute("article", article);
            return "ArticleRead.html";
        }
    }

    @GetMapping("/l/{pageCurr}")
    public String list(
            @PathVariable Integer pageCurr,
            @RequestParam(defaultValue = "5") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        return process(pageCurr, pageSize, session, model, 0, null);
    }

    @GetMapping(
            value = "/c/{category}/{pageCurr}",
            produces = "text/html; charset=utf-8"
    )
    public String listBycategory(
            @PathVariable String category,
            @PathVariable Integer pageCurr,
            @RequestParam(defaultValue = "5") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        return process(pageCurr, pageSize, session, model, 1, category);
    }

    @GetMapping(
            value = "/t/{tag}/{pageCurr}",
            produces = "text/html; charset=utf-8"
    )
    public String listByTag(
            @PathVariable String tag,
            @PathVariable Integer pageCurr,
            @RequestParam(defaultValue = "5") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        return process(pageCurr, pageSize, session, model, 2, tag);
    }

    @PostMapping(value = "search", produces = "text/html; charset=utf-8")
    public String searchAction(String keyword) throws UnsupportedEncodingException {
        LOG.info(String.format("redirect:/a/k/%s", encode(keyword.trim(), "utf-8")));
        return String.format("redirect:/a/k/%s", encode(keyword.trim(), "utf-8"));
    }

    @GetMapping(
            value = "/y/{yearMonth}/{pageCurr}",
            produces = "text/html; charset=utf-8"
    )
    public String listByYearMonth(
            @PathVariable String yearMonth,
            @PathVariable Integer pageCurr,
            @RequestParam(defaultValue = "5") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        return process(pageCurr, pageSize, session, model, 3, yearMonth);
    }

    @GetMapping(
            value = "/k/{keyword}/{pageCurr}",
            produces = "text/html; charset=utf-8"
    )
    public String listByKeyword(
            @PathVariable String keyword,
            @PathVariable Integer pageCurr,
            @RequestParam(defaultValue = "5") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        return process(pageCurr, pageSize, session, model, 4, keyword);
    }

    @RequestMapping(value = {"l", ""}, produces = "text/html; charset=utf-8")
    public String list1(HttpSession session) {
        return "redirect:/a/l/1";//返回文章列表第1页
    }

    @RequestMapping(value = "/c/{s}", produces = "text/html; charset=utf-8")
    public String listByCategory1(@PathVariable String s) throws UnsupportedEncodingException {
        return String.format("redirect:/a/c/%s/1", encode(s.trim(), "utf-8"));
    }

    @RequestMapping(value = "/t/{s}", produces = "text/html; charset=utf-8")
    public String listByTag1(@PathVariable String s) throws UnsupportedEncodingException {
        return String.format("redirect:/a/t/%s/1", encode(s.trim(), "utf-8"));
    }

    @RequestMapping(value = "/y/{s}", produces = "text/html; charset=utf-8")
    public String listByYearMonth1(@PathVariable String s) throws UnsupportedEncodingException {
        return String.format("redirect:/a/y/%s/1", encode(s.trim(), "utf-8"));
    }

    @RequestMapping(value = "/k/{s}", produces = "text/html; charset=utf-8")
    public String listByKeyword1(@PathVariable String s) throws UnsupportedEncodingException {
        return String.format("redirect:/a/k/%s/1", encode(s.trim(), "utf-8"));
    }

    private int getArticleAuth(HttpSession session) {
        ArticleAuth articleAuth = (ArticleAuth) session.getAttribute("articleAuth");
        return articleAuth == null ? 0 : articleAuth.getValue();//查询文章的权限
    }

    /**
     * 处理分页查询
     *
     * @param model SpringMVC Model
     * @param pageCurr 第几页
     * @param pageSize 每页几篇文章
     * @param session Web会话
     * @param selectType
     * @param selectCondition
     *
     * @return 视图名称（template下的xxx.html）
     */
    public String process(
            Integer pageCurr,
            Integer pageSize,
            HttpSession session,
            Model model,
            int selectType,
            String selectCondition
    ) {
        if (selectCondition != null) {
            try {
                selectCondition = URLDecoder.decode(selectCondition, "utf-8");
                LOG.info(selectCondition);
            } catch (UnsupportedEncodingException e) {
                LOG.error(e.toString());
            }
        }
        if (pageCurr == null) {
            pageCurr = 1;
        }

        int auth = getArticleAuth(session);//查询文章的权限

        //查询
        PageInfo pageInfo;//分页
        String selectTitle;//提示信息
        String selectUri;//分页时要返回的Uri,以便再构建链接

        switch (selectType) {
            case 1:
                selectTitle = "分类";
                selectUri = "a/c/";
                pageInfo = articleService.listByCategory(pageCurr, pageSize, auth,
                        (String) selectCondition);
                break;
            case 2:
                selectTitle = "标签";
                selectUri = "a/t/";

                pageInfo = articleService.listByTag(pageCurr, pageSize, auth,
                        (String) selectCondition);
                break;
            case 3:
                selectTitle = "归档";
                selectUri = "a/y/";
                pageInfo = articleService.listByCreateYearMonth(pageCurr, pageSize, auth,
                        DatetimeUtil.formatString((String) selectCondition));
                break;
            case 4:
                selectTitle = "关键词";
                selectUri = "a/k/";
                pageInfo = articleService.listByKeyword(pageCurr, pageSize, auth,
                        (String) selectCondition);
                break;
            default:
                selectTitle = "所有文章";
                selectUri = "a/l";//没有selectCondition,可以接上其后的/号
                pageInfo = articleService.list(pageCurr, pageSize, auth);
                break;
        }

        //添加查询信息
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("selectTitle", selectTitle);
        model.addAttribute("selectCondition", selectCondition);
        model.addAttribute("selectUri", selectUri);

        return "ArticleList.html";
    }

    static enum selectType {
        list, category, tag, yearMonth;
    }
}
