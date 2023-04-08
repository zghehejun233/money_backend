package site.forum.web.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import site.forum.web.auth.mapper.UserMapper;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.AuthError;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.util.AESUtil;
import site.forum.web.common.util.JwtUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Guo Surui
 */
@Service
public class UserService {

    @Resource
    private UserMapper mapper;
    @Resource
    private UserService userService;
   private final AESUtil aesUtil = new AESUtil();

    public User getUserById(String userId) {
        Map<String, Object> result = mapper.findUserByUserId(userId);
        if (result == null) {
            return null;
        }

        User user;
        try {
            user = new User(
                    Long.decode(result.get("id").toString()),
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

    public Result<?> resetPassword(String userId, String newPassword) {
        User user = mapper.selectByUserId(userId);
        if (user == null) {
            return new Result<String>(AuthError.ACCOUNT_NOT_EXISTS);
        }
        mapper.updatePassword(userId,aesUtil.encryptDataWithCasId(user.getUserId(), newPassword));
        return new Result<String>().success("密码重置成功");
    }

    public void updateUserById(User user) {
        mapper.updateById(user);
    }

    /**
     * 解析token并在数据库内查询对象
     *
     * @param token Token
     * @return User.class
     */
    public User getUserByToken(String token) {
        Claims claims = JwtUtil.parseJWT(token);
        User user = getUserById(claims.get("user_id").toString());
        user.setPassword("");

        return user;
    }

    public Result<?> addUsers(List<User> userList) {
        userList.forEach(this::addUser);
        return new Result<>().success("添加成功");
    }

    private void addUser(User user) {
        user.setId(null);
        user.setPassword(aesUtil.encryptDataWithCasId(user.getUserId(), user.getPassword()));
        user.setEmail("");
        mapper.insert(user);
    }
}
