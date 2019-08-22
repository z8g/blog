/*
__        ____  __ ____    ____  _     ___   ____ 
\ \      / /  \/  |  _ \  | __ )| |   / _ \ / ___|
 \ \ /\ / /| |\/| | | | | |  _ \| |  | | | | |  _ 
  \ V  V / | |  | | |_| | | |_) | |__| |_| | |_| |
   \_/\_/  |_|  |_|____/  |____/|_____\___/ \____|
 */
package net.zhaoxuyang.blog.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author zhaoxuyang
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    public ArticleAuthInterceptor articleAuthInterceptor() {
        return new ArticleAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor())
                //                .excludePathPatterns("/user/login/**")//不拦截
                //                .excludePathPatterns("/user/logout")//不拦截
                .addPathPatterns("/user/**");//拦截

        registry.addInterceptor(articleAuthInterceptor())
                .addPathPatterns("/**");

    }

}
