package net.zhaoxuyang.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import net.zhaoxuyang.blog.mapper.ArticleMapper;
import net.zhaoxuyang.blog.model.Article;
import net.zhaoxuyang.blog.model.Conf;
import net.zhaoxuyang.blog.service.ArticleService;
import net.zhaoxuyang.blog.service.UserService;
import net.zhaoxuyang.blog.util.DatetimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhaoxuyang
 */
@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public boolean insert(Article article) {
        article.setId(0);//数据库中已自动递增
        Date now = new Date();//当前时间
        article.setGmtCreate(now);//使用当前时间初始化创建时间
        article.setGmtUpdate(now);//使用当前时间初始化修改时间
        article.createSummary();//根据文章内容创建摘要
        if (article.getAuth() == null) {
            article.setAuth(0);//创建权限为公开可见
        }
        article.setCategory(article.getCategory().trim());

        int n = articleMapper.insert(article);//插入文章
        return n > 0;
    }

    @Override
    public boolean delete(Integer id) {
        Article article = new Article();
        article.setId(id);
        int n = articleMapper.delete(article);
        return n > 0;
    }

    @Override
    public boolean update(Article article) {
        Date now = new Date();
        article.setGmtUpdate(now);
        article.createSummary();
        article.setCategory(article.getCategory().trim());
        if (article.getAuth() == null) {
            article.setAuth(0);
        }
        int n = articleMapper.update(article);
        return n > 0;
    }

    @Override
    public Article get(int auth,Integer id) {
        Article article = new Article();
        article.setId(id);
        article.setAuth(auth);
        System.out.println("###"+article);
        return articleMapper.getById(article);
    }

//    /**
//     * 将tags set 转换成tag set (用|号分开,并且去掉每个set两边的空格)
//     *
//     * @param tagsSet
//     * @return
//     */
//    private static Set<String> formatTagSet(Set<String> tagsSet) {
//        return tagsSet.parallelStream()//并行流
//                .filter((tags) -> (tags != null))//去掉null值
//                .map((tags) -> tags.split("\\|"))//以|号切割
//                .flatMap(Arrays::stream)//将每个数组转换成流
//                .map(tag -> tag.trim())// 去掉空格
//                .collect(toSet());//收集成set
//    }
    @Override
    public PageInfo list(int pageCurr, int pageSize, int auth) {
        Article article = new Article();
        article.setAuth(auth);

        PageHelper.startPage(pageCurr, pageSize);
        List<Article> list = articleMapper.listAnd(article);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo listByKeyword(int pageCurr, int pageSize, int auth,
            String keyword) {
        Article article = new Article();
        article.setAuth(auth);
        article.setTitle(keyword);
        article.setCategory(keyword);
        article.setTags(keyword);
        article.setMarkdown(keyword);

        PageHelper.startPage(pageCurr, pageSize);
        List<Article> list = articleMapper.listOr(article);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo listByCategory(int pageCurr, int pageSize, int auth,
            String category) {
        Article article = new Article();
        article.setAuth(auth);
        article.setCategory(category);

        PageHelper.startPage(pageCurr, pageSize);
        List<Article> list = articleMapper.listAnd(article);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo listByTag(int pageCurr, int pageSize, int auth,
            String tag) {
        Article article = new Article();
        article.setAuth(auth);
        article.setTags(tag);

        PageHelper.startPage(pageCurr, pageSize);
        List<Article> list = articleMapper.listAnd(article);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo listByCreateYearMonth(int pageCurr, int pageSize, int auth,
            Date date) {
        Article article = new Article();
        article.setAuth(auth);
        article.setGmtCreate(date);

        PageHelper.startPage(pageCurr, pageSize);
        List<Article> list = articleMapper.listAnd(article);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public Map<String, Integer> getCategoryMap(int auth) {
        return getCategoryMap(listAll(auth));
    }

    @Override
    public Map<String, Integer> getYearMonthMap(int auth) {
        return getYearMonthMap(listAll(auth));
    }

    @Override
    public Set<String> getTagSet(int auth) {
        return getTagSet(listAll(auth));
    }

    private List<Article> listAll(int auth) {
        Article article = new Article();
        article.setAuth(auth);
        return articleMapper.listAnd(article);
    }

    private static Map<String, Integer> getCategoryMap(List<Article> articleList) {
        Map<String, Integer> categoryMap = new HashMap<>();
        for (Article article : articleList) {
            String key = article.getCategory();
            if (categoryMap.containsKey(key)) {
                Integer value = categoryMap.get(key);
                categoryMap.put(key, value + 1);
            } else {
                categoryMap.put(key, 1);
            }
        }
        return categoryMap;
    }

    private static Set<String> getTagSet(List<Article> articleList) {
        Set<String> tagSet = new TreeSet<>();
        for (Article article : articleList) {
            String tags = article.getTags();
            if (tags != null) {
                String[] tagArray = tags.split("\\|");
                for (String tag : tagArray) {
                    String trimTag = tag.trim();
                    if (trimTag.length() > 0) {
                        tagSet.add(tag);
                    }
                }
            }
        }
        return tagSet;
    }

    private static Map<String, Integer> getYearMonthMap(List<Article> articleList) {
        Map<String, Integer> yearMonthMap = new HashMap<>();
        for (Article article : articleList) {
            Date gmtUpdate = article.getGmtUpdate();
            String key = DatetimeUtil.formatDate(gmtUpdate);
            if (yearMonthMap.containsKey(key)) {
                Integer value = yearMonthMap.get(key);
                yearMonthMap.put(key, value + 1);
            } else {
                yearMonthMap.put(key, 1);
            }
        }
        return yearMonthMap;
    }

    @Autowired UserService userService;
    
    @Override
    public String createRssXml(HttpServletRequest request) {
        StringBuilder result = new StringBuilder();
        
        PageInfo pageInfo = list(1, 100, 0);
        Map<String,Conf> confMap = userService.getConfMap();
        
        
        List<Article> articleList = pageInfo.getList();

        String webSiteUrl =getWebsiteUrl(request);
        
        String channelTitle = confMap.get("website.title").getValue();
        String imageLink = webSiteUrl;
        String imageUrl = confMap.get("website.user.headImg").getValue();
        String description = confMap.get("website.meta.description").getValue();
        String link = webSiteUrl;
        String language = "zh-cn";
        String generator = webSiteUrl;
        String copyright = confMap.get("user.nickname").getValue();
        String pubDate = DatetimeUtil.formatDate(new Date());
        String username = confMap.get("user.username").getValue();
        StringBuffer items = new StringBuffer();
        for (Article article : articleList) {
            items.append("<item>")
                    .append(String.format("<title><![CDATA[%s]]></title>", article.getTitle()))
                    .append(String.format("<link>%s/a/%d</link>", webSiteUrl,article.getId()))
                    .append(String.format("<guid>%s/a/%d</guid>", webSiteUrl,article.getId()))
                    .append(String.format("<author>%s</author>",username))
                    .append(String.format("<pubDate>%s</pubDate>",DatetimeUtil.formatDate(article.getGmtUpdate())))
                    .append(String.format("<description><![CDATA[%s]]></description>",article.getSummary()))
                    .append(String.format("<category>%s</category>",article.getCategory()))
                    .append("</item>");
        }

        result.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n")
                .append(String.format("<?xml-stylesheet type=\"text/xsl\" title=\"XSL Formatting\" href=\"%s/assets/css/rss.xsl\" media=\"all\" ?>\n",getWebsiteUrl( request)))
                .append("<rss version=\"2.0\">")
                .append("<channel>")
                .append("<title>").append(channelTitle).append("</title>")
                .append("<image>")
                .append("<link>").append(imageLink).append("</link>")
                .append("<url>").append(imageUrl).append("</url>")
                .append("</image>")
                .append("<description>").append(description).append("</description>")
                .append("<link>").append(link).append("</link>")
                .append("<language>").append(language).append("</language>")
                .append("<generator>").append(generator).append("</generator>")
                .append("<ttl>5</ttl>")
                .append("<copyright><![CDATA[Copyright &copy; ").append(copyright).append("]]></copyright>")
                .append("<pubDate>").append(pubDate).append("</pubDate>")
                .append(items)
                .append("</channel>")
                .append("</rss>");
        
        return result.toString();
    }

    
    private static String getCurrentUrl(HttpServletRequest request){
        return String.format("%s%s", getWebsiteUrl(request),getWebsiteUri(request));
    }
    
    private static String getWebsiteUrl(HttpServletRequest request) {
        return String.format("%s://%s:%d", request.getScheme(),
                request.getServerName(), request.getServerPort());
    }

    private static String getWebsiteUri(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        int index = requestURI.lastIndexOf("/");
        return index <= 2 ? requestURI : requestURI.substring(0, index);

    }

}
