package site.forum.web.common.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.util.Base64Util;
import site.forum.web.common.util.LimitCacheUtil;
import site.forum.web.common.annotation.AccessLimit;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            log.info("accessLimit: {}", accessLimit);
            if (accessLimit == null) {
                return true;
            }
            int maxCount = accessLimit.maxCount();
            String key = Base64Util.encode(
                    request.getRequestURI() +
                            request.getRemoteAddr() +
                            request.getHeader("token")
            );

            Integer requestCount = LimitCacheUtil.getRequestCount(key, accessLimit.seconds());
            log.info("requestCount: {}", requestCount);

            if (requestCount < maxCount) {
                return true;
            } else {
                log.warn("requestCount: {} is too much.", requestCount);
                response.setContentType("application/json;charset=UTF-8");
                OutputStream out = response.getOutputStream();
                String str = JSON.toJSONString(new Result<>().fail("访问频率太高啦，休息一下再来吧x"));
                out.write(str.getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
