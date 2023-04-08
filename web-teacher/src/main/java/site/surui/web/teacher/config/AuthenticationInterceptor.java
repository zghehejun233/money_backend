package site.surui.web.teacher.config;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.surui.web.common.data.po.User;
import site.surui.web.common.util.JwtUtil;
import site.surui.web.teacher.mapper.TeacherMapper;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String token = request.getHeader("token");
        try {
            JwtUtil.parseJWT(token);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }

        Claims claims = JwtUtil.parseJWT(token);
        User user = new User(teacherMapper.getTeacherId(claims.get("user_id").toString()),
                claims.get("user_id").toString(),
                null,
                claims.get("email").toString(),
                Integer.parseInt(claims.get("role").toString()));
        request.setAttribute("USER_INFO", user);
        if (user.getRole() != 1) {
            try {
                response.sendError(401);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
