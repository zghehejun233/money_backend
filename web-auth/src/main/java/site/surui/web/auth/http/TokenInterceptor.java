package site.surui.web.auth.http;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.surui.web.auth.util.UserContext;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.util.JwtUtil;

import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        // 1.从Cookie获取token
        String token = getTokenFromCookie(request);
        if (token == null || token.isBlank() || token.isEmpty()) {
            token = request.getHeader("token");
        }
        if (token == null || token.isBlank() || token.isEmpty()) {
            // 3.从请求参数获取
            token = request.getParameter("token");
        }
        if (token == null || token.isBlank() || token.isEmpty()) {
            //输出响应流
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "403");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getOutputStream().write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            return false;
        }

        // 验证token
        Result<?> checkResult = JwtUtil.validateJWT(token);
        if (checkResult.getCode() == new Result<>().success().getCode()) {
            // 向ThreadLocal中添加token信息
            UserContext.put(token);
            return true;
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "403");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getOutputStream().write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            return false;
        }
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        UserContext.remove();
    }

    /**
     * 从Cookie中获取Token
     *
     * @param request 请求
     * @return String
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        int len = null == cookies ? 0 : cookies.length;
        if (len > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
