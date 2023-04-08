package site.surui.web.auth.service;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import site.surui.web.auth.data.dto.LoginDto;
import site.surui.web.auth.data.dto.LoginResponse;
import site.surui.web.auth.mapper.UserMapper;
import site.surui.web.auth.util.FileUtil;
import site.surui.web.auth.util.UserContext;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.AuthError;
import site.surui.web.common.data.vo.result.CommonError;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.util.AESUtil;
import site.surui.web.common.util.Base64Util;
import site.surui.web.common.util.JwtUtil;

import javax.annotation.Resource;
import java.awt.*;
import java.util.*;

/**
 * @author Guo Surui
 */
@Service
@RefreshScope
@Slf4j
public class AuthService {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    private final AESUtil aesUtil = new AESUtil();

    @Value("${image.upload}")
    String picUploadUrl;
    @Value("${image.direct}")
    String picDirectUrl;
    @Value("${image.username}")
    String cloudUserName;
    @Value("${image.password}")
    String cloudPassword;

    public final static String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";
    final Cache<String, String> codeCache = CacheUtil.newFIFOCache(100);

    /**
     * 登录，直接返回包装的 Result
     *
     * @param loginDto 登录数据
     * @return Result
     */
    public Result<?> login(LoginDto loginDto) {
        if (loginDto == null) {
            return new Result<String>(CommonError.PARAM_NOT_FOUND);
        }

        User user = userService.getUserById(loginDto.getUserId());
        log.info("user: {}", user);
        if (user == null) {
            return new Result<String>(AuthError.ACCOUNT_NOT_EXISTS);
        }

        // 判断是否和数据库中加密的密码相同
        String encryptedPassword = aesUtil.encryptDataWithCasId(user.getUserId(), loginDto.getPassword());
        if (encryptedPassword.equals(user.getPassword())) {
            switch (user.getRole()) {
                case 2:
                    if (userMapper.isStudentExist(user.getUserId()) == null) {
                        userMapper.syncStudentInfo(user.getUserId());
                        log.info("同步学生信息成功");
                    }
                    user.setId(userMapper.getStudentIdByUserId(user.getUserId()));
                    break;
                case 1:
                    if (userMapper.isTeacherExist(user.getUserId()) == null) {
                        userMapper.syncTeacherInfo(user.getUserId());
                        log.info("Sync Teacher.");
                    }
                    user.setId(userMapper.getTeacherIdByUserId(user.getUserId()));
                    break;
                default:
                    log.error("用户角色错误");
            }
            // 生成token
            String token = JwtUtil.generateJWT(String.valueOf(new Date()), user, (long) (1000 * 60 * 60 * 24 * 10));
            LoginResponse responseData = new LoginResponse(token, user.getRole(),user.getEmail());
            return new Result<LoginResponse>().success("登录成功", responseData);
        } else {
            return new Result<String>(AuthError.PASSWORD_WRONG);
        }
    }


    public Result<?> changePassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) return new Result<String>(AuthError.DUPLICATED_PASSWORD);
        String token = UserContext.get();

        if (token == null || token.isBlank() || token.isEmpty()) {
            return new Result<String>(AuthError.LOGIN_STATUS_WRONG);
        }
        String userId = JwtUtil.parseJWT(token).get("user_id").toString();
        User user = userService.getUserById(userId);
        if (Objects.equals(aesUtil.encryptDataWithCasId(user.getUserId(), oldPassword), user.getPassword())) {//旧密码正确
            user.setPassword(aesUtil.encryptDataWithCasId(userId, newPassword));
            userService.updateUserById(user);
            return new Result<String>().success("修改密码成功，请重新登录");
        } else return new Result<String>(AuthError.WRONG_OLD_PASSWORD);//旧密码错误

    }

    public Result<?> resetPassword(LoginDto loginDto, String code, String token) {
        User user = userService.getUserById(loginDto.getUserId());
        if (user == null) {
            return new Result<String>(AuthError.ACCOUNT_NOT_EXISTS);
        }
        if (verifyCode(code, token).getCode() != 0) {
            return new Result<String>(AuthError.VERIFY_CODE_WRONG);
        }

        if (Objects.equals(aesUtil.encryptDataWithCasId(user.getUserId(), loginDto.getPassword()), user.getPassword())) {
            return new Result<String>(AuthError.DUPLICATED_PASSWORD);
        }

        user.setPassword(aesUtil.encryptDataWithCasId(user.getUserId(), loginDto.getPassword()));
        userService.updateUserById(user);
        return new Result<String>().success("密码重置成功");
    }

    public Result<?> forgetPassword(String userId, String email) {
        User user = userService.getUserById(userId);
        if (user == null)
            return new Result<String>(AuthError.ACCOUNT_NOT_EXISTS);
        if (user.getEmail().equals("")) return new Result<String>(AuthError.EMAIL_NULL);
        if (!user.getEmail().equals(email)) return new Result<String>(AuthError.WRONG_EMAIL);
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(aesUtil.encryptDataWithCasId(user.getUserId(), randomPassword));
        userService.updateUserById(user);
        MailUtil.send(email, "布鸽管理系统绑定邮箱验证", "<h3>您的临时密码为:"+randomPassword+"<br/>请尽快登录并修改密码！</h3>", true);
        return new Result<String>().success("已发送临时密码到您的邮箱");
    }

    public Result<?> createCode(String userId) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        Image image = lineCaptcha.getImage();

        FileUtil fileUtil = new FileUtil(cloudUserName, cloudPassword);
        String imageUrl = fileUtil.uploadCode(lineCaptcha.getImageBytes(), picUploadUrl, picDirectUrl, userId);
        String code = lineCaptcha.getCode();
        String fileNameToken = Base64Util.encode(userId + DateUtil.now());

        codeCache.put(fileNameToken, code);
        log.debug(codeCache.get(fileNameToken));

        Map<String, String> result = new HashMap<>();
        result.put("url", imageUrl);
        result.put("token", fileNameToken);

        return new Result<Map<String, String>>().success(result);
    }

    public Result<?> verifyCode(String code, String token) {
        String savedCode = codeCache.get(token);
        if (token == null) {
            return new Result<String>().fail("未找到对应的记录");
        }
        return (code.equals(savedCode)) ? new Result<>().success() : new Result<>(AuthError.VERIFY_CODE_WRONG);
    }
}
