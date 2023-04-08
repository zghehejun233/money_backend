package site.surui.web.auth.service;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.extra.mail.MailUtil;
import org.springframework.stereotype.Service;
import site.surui.web.auth.data.dto.MailDto;
import site.surui.web.auth.util.UserContext;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.AuthError;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.util.JwtUtil;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 本Service用于实现邮件发送业务
 */
@Service
public class MailService {

    final Cache<String, String> codeCache = CacheUtil.newFIFOCache(100);
    final Cache<String, String> emailCache = CacheUtil.newFIFOCache(100);

    @Resource
    private UserService userService;

    public void sendEmail(MailDto mail) {
        MailUtil.send(mail.getTo(), mail.getSubject(), mail.getContent(), mail.isHtml());
    }

    public Result<?> bindMailSendCode(String email) {
        String token = UserContext.get();
        if (token == null || token.isBlank() || token.isEmpty())
            return new Result<String>(AuthError.LOGIN_STATUS_WRONG);
        String userId = JwtUtil.parseJWT(token).get("user_id").toString();
        User user = userService.getUserById(userId);
        if (!user.getEmail().equals("")) return new Result<String>(AuthError.EMAIL_NOT_NULL);
        String code = UUID.randomUUID().toString().substring(0, 8);
        codeCache.put(userId, code);
        emailCache.put(userId, email);
        MailUtil.send(email, "布鸽管理系统绑定邮箱验证", "<h3>您的验证码为:"+code+"</h3>", true);
        return new Result<String>().success("已发送验证码到" + email);
    }

    public Result<?> verifyCode(String code) {
        String token = UserContext.get();
        if (token == null || token.isBlank() || token.isEmpty())
            return new Result<String>(AuthError.LOGIN_STATUS_WRONG);
        String userId = JwtUtil.parseJWT(token).get("user_id").toString();
        User user = userService.getUserById(userId);
        if (!user.getEmail().equals(""))return new Result<String>(AuthError.EMAIL_NOT_NULL);
        String cacheCode = codeCache.get(userId);
        if (cacheCode.equals(code)) {
            user.setEmail(emailCache.get(userId));
            userService.updateUserById(user);
            return new Result<String>().success("绑定邮箱成功");
        } else {
            return new Result<String>(AuthError.VERIFY_CODE_WRONG);
        }


    }

}
