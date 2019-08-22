package net.zhaoxuyang.blog;

import java.lang.invoke.MethodHandles;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Beetl视图技术 - 配置类
 *
 * @author zhaoxuyang
 */
@Configuration
public class BeetlConfig {

    static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * @Value 表示从application.properties中取键
     */
    @Value("${beetl.templatesPath}")
    String templatesPath;//模板根目录 ，比如 "templates"

    /**
     * 添加共享变量
     *
     * @param beetlGroupUtilConfiguration
     * @return
     */
    @Bean
    public GroupTemplate getGroupTemplate(BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        LOG.info("GroupTemplate getGroupTemplate(BeetlGroupUtilConfiguration beetlGroupUtilConfiguration)");
        LOG.info("beetlGroupUtilConfiguration=" + beetlGroupUtilConfiguration);

        GroupTemplate gt = beetlGroupUtilConfiguration.getGroupTemplate();
        Map<String, Object> shared = new HashMap<>();
        shared.put("blogCreateUser", "zhaoxuyang");
        gt.setSharedVars(shared);
        return gt;
    }

    /**
     * 配置Beetl
     *
     * @return
     */
    @Bean
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        //获取Spring Boot 的ClassLoader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = BeetlConfig.class.getClassLoader();
        }
        ClasspathResourceLoader cploder = new ClasspathResourceLoader(loader, templatesPath);
        beetlGroupUtilConfiguration.setResourceLoader(cploder);
        beetlGroupUtilConfiguration.init();
        //如果使用了优化编译器，涉及到字节码操作，需要添加ClassLoader
        beetlGroupUtilConfiguration.getGroupTemplate().setClassLoader(loader);
        return beetlGroupUtilConfiguration;

    }

    /**
     * Beetl视图
     *
     * @param beetlGroupUtilConfiguration
     * @return
     */
    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }
}
