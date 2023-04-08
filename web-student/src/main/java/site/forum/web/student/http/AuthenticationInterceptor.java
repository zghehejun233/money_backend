package site.forum.web.student.http;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.forum.web.student.mapper.StudentMapper;
import site.forum.web.student.mapper.UserMapper;
import site.forum.web.common.data.po.User;
import site.forum.web.common.util.JwtUtil;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 拦截器
 *
 * 用于通过token向auth服务执行远程调用获取用户信息
 * 如果用户信息有问题抛出异常，否则向request中写入信息
 */
@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper mapper;
    @Resource
    private StudentMapper studentMapper;
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String token = request.getHeader("token");

        try {
            JwtUtil.parseJWT(token);
        } catch (ExpiredJwtException expiredJwtException) {
            expiredJwtException.printStackTrace();
            return false;
        } catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }

        Claims claims = JwtUtil.parseJWT(token);
        User user = getUserById(claims.get("user_id").toString());
        user.setPassword("");
        log.info("user: {}", user);
        request.setAttribute("USER_INFO", user);
        return true;
    }

    private User getUserById(String userId) {
        Map<String, Object> result = mapper.findUserByUserId(userId);
        // Student student = studentMapper.getStudentInfo(userId);
        User user;
        try {
            user = new User(
                    studentMapper.getIdByUserId(userId),
                    result.get("user_id").toString(),
                    result.get("pwd").toString(),
                    result.get("email").toString(),
                    Integer.decode(result.get("r").toString()
                    ));
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            user = new User();
        }
        return user;
    }
}
