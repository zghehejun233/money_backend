package site.surui.web.admin.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import site.surui.web.admin.service.AccountService;
import site.surui.web.admin.service.UserRemoteService;
import site.surui.web.common.data.po.Student;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.data.po.Teacher;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private UserRemoteService userRemoteService;
    @Resource
    private AccountService accountService;

    @ApiOperation(value = "重置用户密码")
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public Result<?> resetPassword(@RequestParam("user") String userId) {
        return userRemoteService.resetUserPassword(userId, "123456");
    }

    @ApiOperation(value = "删除用户")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Result<?> deleteUser(@RequestParam("id") Long userId) {
        return accountService.deleteUser(userId);
    }

    @ApiOperation(value = "获取所有用户信息")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Result<?> getUserInfo(@RequestParam("role") int role) {
        return accountService.getAllUserInfo(role);
    }

    @ApiOperation(value = "添加新的教师用户")
    @RequestMapping(value = "/teacher", method = RequestMethod.PUT)
    public Result<?> addNewTeacher(@RequestBody Teacher teacher, @RequestParam("password") String password) {
        return accountService.addNewTeacher(teacher, password);
    }

    @ApiOperation(value = "删除教师用户")
    @RequestMapping(value = "/teacher", method = RequestMethod.DELETE)
    public Result<?> deleteTeacher(@RequestParam("id") Long userId) {
        return accountService.deleteTeacher(userId);
    }

    @ApiOperation(value = "编辑教师用户")
    @RequestMapping(value = "/teacher", method = RequestMethod.POST)
    public Result<?> editTeacher(@RequestBody JSONObject data, @RequestParam("id") Long userId) {
        return accountService.editTeacherInfo(data, userId);
    }

     @ApiOperation(value = "添加新的学生用户")
     @RequestMapping(value = "/student", method = RequestMethod.PUT)
     public Result<?> addNewStudent(@RequestBody Student student) {
         return accountService.addNewStudent(student, "123456");
     }

    @ApiOperation(value = "删除学生用户")
    @RequestMapping(value = "/student", method = RequestMethod.DELETE)
    public Result<?> deleteStudent(@RequestParam("id") Long id) {
        return accountService.deleteStudent(id);
    }

    @ApiOperation(value = "编辑学生用户")
    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public Result<?> editStudent(@RequestBody JSONObject data, @RequestParam("id") Long id) {
        return accountService.editStudentInfo(data, id);
    }

    @ApiOperation(value = "获取所有班级")
    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public Result<?> getAllClass() {
        return accountService.getAllClass();
    }

    @ApiOperation(value = "获取所有班级")
    @RequestMapping(value = "/class", method = RequestMethod.PUT)
    public Result<?> addAClass(@RequestParam("grade") String grade, @RequestParam("className") String className) {
        return accountService.addAClass(grade, className);
    }
}
