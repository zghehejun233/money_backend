package site.surui.web.teacher.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.surui.web.common.data.po.User;
import site.surui.web.teacher.data.annotations.TeacherInfo;

import javax.annotation.Nonnull;

@Component
public class InfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(User.class) &&
                parameter.hasParameterAnnotation(TeacherInfo.class);
    }

    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        return webRequest.getAttribute("USER_INFO", RequestAttributes.SCOPE_REQUEST);
    }
}
