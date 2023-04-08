package site.forum.web.auth.controller;

import cn.hutool.captcha.LineCaptcha;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.forum.web.auth.data.dto.LoginDto;
import site.forum.web.auth.service.AuthService;
import site.forum.web.auth.service.MailService;
import site.forum.web.common.annotation.AccessLimit;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;

/**
 * @author Guo Surui
 */
@RestController
@Api(value = "鉴权业务接口")
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private MailService mailService;


    /**
     * 登录
     *
     * @param loginDto 登录DTO
     * @return 结果
     */
    @ApiOperation(value = "用户登录", notes = "---已测试---")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    public Result<?> login(@RequestBody @ApiParam(value = "用户登录信息", required = true) LoginDto loginDto) {
        log.debug("loginDto: {}", loginDto);
        return authService.login(loginDto);
    }

    /**
     * 修改密码：传入旧密码和新密码，旧密码正确时，执行修改密码操作
     *
     * @param oldPassword 旧密码
     * @param newPassword 新的密码
     * @return 结果
     */
    @ApiOperation(value = "修改密码", notes = "---已测试---")
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    public Result<?> changePassword(@RequestParam @ApiParam("旧的密码") String oldPassword, @RequestParam @ApiParam("新的密码") String newPassword) {
        return authService.changePassword(oldPassword, newPassword);
    }

    /**
     * 绑定邮箱-向邮箱发送验证码：携带token，输入邮箱，将向该邮箱发送一条验证码
     *
     * @param email 邮箱
     * @return 结果
     */
    @ApiOperation(value = "绑定邮箱", notes = "---已测试---")
    @RequestMapping(value = "/email/bind", method = RequestMethod.GET)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    @AccessLimit(maxCount = 3, seconds = 10)
    public Result<?> bindMailSendCode(@RequestParam @ApiParam("邮箱") String email) {
        return mailService.bindMailSendCode(email);
    }


    /**
     * 绑定邮箱-校验验证码：携带token，传入验证码，如果验证码正确，则成功绑定
     *
     * @param code 验证码
     * @return 结果
     */
    @ApiOperation(value = "绑定邮箱", notes = "---已测试---")
    @RequestMapping(value = "/email/verify", method = RequestMethod.POST)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    @AccessLimit
    public Result<?> verifyCode(@RequestParam @ApiParam("验证码") String code) {
        return mailService.verifyCode(code);
    }

    /**
     * 重置密码：验证密码后向用户返回一个图形验证码
     *
     * @param loginDto 信息
     * @return 结果
     */
    @ApiOperation(value = "重置密码接口", notes = "---已测试---\n需要正常的登录信息，校验一个图形验证码；需要先请求一个图形验证码")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    public Result<?> resetPassword(@RequestBody @ApiParam("登录信息") LoginDto loginDto,
                                   @RequestParam("code") @ApiParam("验证码") String code,
                                   @RequestParam("token") String token) {
        return authService.resetPassword(loginDto, code, token);
    }

    /**
     * 忘记密码：向邮箱发送一个新的密码
     *
     * @param userId 学工号
     * @param email  邮箱
     * @return x
     */
    @ApiOperation(value = "忘记密码", notes = "---已测试---\n后台检验邮箱一致后，向邮箱发送一个新的默认密码")
    @RequestMapping(value = "/forget", method = RequestMethod.GET)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    @AccessLimit(maxCount = 3, seconds = 10)
    public Result<?> forgetPassword(@RequestParam @ApiParam("学工号") String userId, @RequestParam @ApiParam("邮箱") String email) {
        return authService.forgetPassword(userId, email);
    }


    @ApiOperation(value = "获取图形验证码", notes = "---已测试---")
    @RequestMapping(value = "/code/image", method = RequestMethod.GET)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "图形验证码", response = LineCaptcha.class)}
    )
    public Result<?> createCode(@RequestParam("user") String userId) {
        return authService.createCode(userId);
    }

    /**
     * 检验验证码
     *
     * @param code 输入的code
     * @return x
     */
    @ApiOperation(value = "校验图形验证码", notes = "---已测试---")
    @RequestMapping(value = "/code/verify", method = RequestMethod.GET)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    public Result<?> verifyCode(@RequestParam("code") @ApiParam("输入的验证码字符") String code,
                                @RequestParam("token") String token) {
        return authService.verifyCode(code, token);
    }
}
