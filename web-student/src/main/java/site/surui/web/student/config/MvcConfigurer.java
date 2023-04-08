package site.surui.web.student.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.surui.web.student.http.AuthenticationInterceptor;
import site.surui.web.student.http.StudentArgumentResolver;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
    @Resource
    private AuthenticationInterceptor authenticationInterceptor;
    @Resource
    private StudentArgumentResolver studentArgumentResolver;

    // 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/info/**", "/activity/**", "/study/**", "/select/**");
    }

    // 配置参数解析器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(studentArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }
}