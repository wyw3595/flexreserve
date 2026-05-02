package com.flexreserve.config;

import com.flexreserve.interceptor.LoginInterceptor;
import com.flexreserve.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**").order(0);

        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/code",
                        "/api/user/register",
                        "/api/resource/list",
                        "/api/resource/{resourceId}/slots",
                        // Knife4j 页面和静态资源
                        "/doc.html",        // 文档主页
                        "/webjars/**",      // JS/CSS 静态资源
                        "/v3/api-docs/**", // OpenAPI 3 规范文档
                        "/swagger-resources/**", // Swagger 资源
                        "/favicon.ico",     // 浏览器图标

                        // Spring 错误页面（可选，友好一些）
                        "/error"
                ).order(1);
    }
}
