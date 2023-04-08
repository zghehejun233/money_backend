package site.forum.web.student.http;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.forum.web.common.data.po.User;

import javax.annotation.Nonnull;

/**
 * 请求方法参数解析器
 */
@Component
public class StudentArgumentResolver implements HandlerMethodArgumentResolver {
    // 对参数进行条件过滤，符合条件返回true，执行下面的resolveArgument方法
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 判断参数是否为User.class且被指定了UserInfo.class注解
        return parameter.getParameterType().isAssignableFrom(User.class) && parameter.hasParameterAnnotation(StudentInfo.class);
    }

    // 解析参数
    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        // 返回拦截器写入到header的用户信息字段
        return webRequest.getAttribute("USER_INFO", RequestAttributes.SCOPE_REQUEST);
    }
}
