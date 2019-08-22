package net.zhaoxuyang.blog.service;

import com.github.pagehelper.PageInfo;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import net.zhaoxuyang.blog.model.Article;

/**
 *
 * @author zhaoxuyang
 */
public interface ArticleService {

    public boolean insert(Article article);//插入

    public boolean update(Article article);//修改

    public boolean delete(Integer id);//删除

    public Article get(int auth, Integer id);//根据文章ID获取一个用户的一篇文章

    public Map<String,Integer> getCategoryMap(int auth);//列出所有个人分类以及数量
    public Map<String,Integer> getYearMonthMap(int auth);//列出所有归档以及数量
    public Set<String> getTagSet(int auth);//列出所有标签

    public PageInfo list(int pageCurr, int pageSize, int auth);//列出所有文章

    public PageInfo listByKeyword(int pageCurr, int pageSize, int auth,
            String keyword);//全局搜索，根据标题或内容查找所有文章

    public PageInfo listByCategory(int pageCurr, int pageSize, int auth,
            String category);//根据个人分类查找所有文章

    public PageInfo listByCreateYearMonth(int pageCurr, int pageSize, int auth,
            Date date);//根据创建的月份列出所有的文章

    public PageInfo listByTag(int pageCurr, int pageSize, int auth,
            String tag);//根据标签查找一个用户的所有文章
    
    public String createRssXml(HttpServletRequest request);

}
