package site.surui.web.auth.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import site.surui.web.auth.service.UserService;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;

import javax.annotation.Resource;
import java.util.List;

@Api("用户服务")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation(value = "批量添加用户", notes = "---已测试---\n传入一个json数组，批量初始化用户；密码默认为123456")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "OK", response = Result.class)}
    )
    public Result<?> addUser(@RequestBody @ApiParam("需要添加的用户") List<User> userList) {
        return userService.addUsers(userList);
    }

    @ApiOperation(value = "重置用户密码")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public Result<?> resetPassword(@RequestParam("id") @ApiParam("需要重置密码的用户") String userId,
                                   @RequestParam("password") @ApiParam("新密码") String newPassword) {
        return userService.resetPassword(userId, newPassword);
    }

    @ApiOperation(value = "获取用户信息", notes = "---已测试---主要供远程调用， 接受token")
    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取成功", response = Result.class)})
    public Result<?> getUser(@PathVariable @ApiParam(value = "token", required = true) String token) {
        return new Result<>().success(JSON.toJSONString(userService.getUserByToken(token)));
    }
}
