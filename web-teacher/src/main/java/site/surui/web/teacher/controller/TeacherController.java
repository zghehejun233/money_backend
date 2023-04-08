package site.surui.web.teacher.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.teacher.data.annotations.TeacherInfo;
import site.surui.web.teacher.data.po.Paper;
import site.surui.web.teacher.service.TeacherService;

import javax.annotation.Resource;

@Api("教师服务接口")
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private TeacherService teacherService;

    @ApiOperation(value = "获取教师信息", notes = "已测试")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result<?> getTeacherInfo(@TeacherInfo @ApiParam(hidden = true) User user) {
        return teacherService.getTeacherInfo(user.getId(), user.getEmail());
    }

    @ApiOperation(value = "编辑教师信息", notes = "已测试")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public Result<?> editTeacherInfo(@TeacherInfo @ApiParam(hidden = true) User user,
                                     @RequestParam(required = false) @ApiParam(value = "要修改信息的教师工号") Long id,
                                     @RequestBody @ApiParam(value = "编辑信息", required = true) JSONObject data) {
        return teacherService.edit((id == null) ? user.getId() : id, data);
    }

    @ApiOperation(value = "上传头像")
    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    public Result<?> uploadAvatar(@TeacherInfo @ApiParam(hidden = true) User user,
                                  @RequestParam @ApiParam(value = "头像文件", required = true) MultipartFile file) {
        return teacherService.uploadAvatar(user.getId(), user.getUserId(), file);
    }

    @ApiOperation(value = "获取所有的论文成果", notes = "已测试")
    @RequestMapping(value = "/paper", method = RequestMethod.GET)
    public Result<?> getPapers(@TeacherInfo @ApiParam(hidden = true) User user) {
        return teacherService.getPapers(user.getUserId());
    }

    @ApiOperation(value = "添加一条论文成果", notes = "已测试")
    @RequestMapping(value = "/paper", method = RequestMethod.PUT)
    public Result<?> putPaper(@TeacherInfo @ApiParam(hidden = true) User user,
                              @RequestBody @ApiParam(value = "paper信息内容", required = true) Paper content) {
        return teacherService.addPaper(user.getId(), content);
    }

    @ApiOperation(value = "移除一条论文成果", notes = "已测试")
    @RequestMapping(value = "/paper", method = RequestMethod.DELETE)
    public Result<?> deletePaper(@RequestParam("id") @ApiParam("要删除的论文id") Long id) {
        return teacherService.removePaper(id);
    }
}
