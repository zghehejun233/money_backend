package site.forum.web.student.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.forum.web.student.service.TagService;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.student.http.StudentInfo;
import site.forum.web.student.service.CommentService;
import site.forum.web.student.service.StudentInfoService;

import javax.annotation.Resource;


@RestController
@Api("学生个人信息")
@RequestMapping("/info")
public class StudentInfoController {
    @Resource
    private StudentInfoService studentInfoService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;

    @ApiOperation(value = "获取特定学生信息", notes = "---已测试---\n接受一个useId作为查询参数")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取成功", response = Result.class)})
    public Result<?> getStudentInfo(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo) {
        return studentInfoService.getStudentInfo(userInfo.getUserId());
    }

    @ApiOperation(value = "获取所有学生信息", notes = "---没想好---\n以分页的形似获取所有信息（还没想好）")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Result<?> getAllStudentInfo(@RequestParam("page") Integer page, @RequestParam("??") Integer xx) {
        return studentInfoService.getAllStudent(page, xx);
    }

    @ApiOperation(value = "修改学生信息", notes = "---已测试---\n前端传一个JSON")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "获取成功", response = Result.class)})
    public Result<?> getStudentInfo(@StudentInfo @ApiParam(hidden = true) User userInfo,
                                    @RequestBody @ApiParam(value = "修改的数据", required = true) JSONObject data) {
        return studentInfoService.editStudentInfo(userInfo.getUserId(), data);
    }

    @ApiOperation(value = "上传头像", notes = "---未实现---\n等前端上传头像再说www")
    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    @ApiResponses({@ApiResponse(code = 200, message = "上传成功", response = Result.class)})
    public Result<?> uploadAvatar(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo,
                                  @RequestParam("avatar") MultipartFile avatar) {
        return studentInfoService.uploadAvatar(userInfo.getUserId(), avatar);
    }


    @ApiOperation(value = "获取培养计划", notes = "---已测试---\n挺好的，能用")
    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public Result<?> getEducationPlan(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "type", required = false) String type,
                                      @RequestParam(value = "state", required = false) Integer state,
                                      @RequestParam(value = "high", required = false) Integer high,
                                      @RequestParam(value = "low", required = false) Integer low) {
        return studentInfoService.getEducationPlan(userInfo.getUserId(), name, type, state, high, low);
    }

    @ApiOperation(value = "按班级设置培养计划", notes = "---还没写---\n计划提供给管理员，将所有班级号一致的设定同一个培养计划")
    @RequestMapping(value = "/plan/set", method = RequestMethod.POST)
    public Result<?> setEducationPlansByClass(@RequestParam("class") @ApiParam("班级号") String classId) {
        return new Result<String>().fail();
    }

    @ApiOperation(value = "更新一个学生的标签", notes = "---已测试---未被定义过的标签在数据库中增加记录；定义过的建立关系并初始化关系为1；有关系的将权重+1")
    @RequestMapping(value = "/tags/update", method = RequestMethod.POST)
    public Result<?> updateTags(@RequestParam("userId") @ApiParam("被添加记录的学生学号") String userId,
                                @RequestBody @ApiParam("一个json列表") JSONObject object) {
        return tagService.updateTags(userId, object);
    }

    @ApiOperation(value = "获取一个学生所有的标签及其权重", notes = "---已测试---")
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public Result<?> getTags(@StudentInfo User student) {
        return tagService.getTags(student.getUserId());
    }


    @ApiOperation(value = "添加对一个学生的互评信息")
    @RequestMapping(value = "/comment/add", method = RequestMethod.PUT)
    public Result<?> addComment(@StudentInfo User user,
                                @RequestParam("id") @ApiParam("被添加记录的学生学号") Long id,
                                @RequestBody @ApiParam("评论内容") JSONObject data) {
        return commentService.addComment(user.getId(), id, data);
    }

    @ApiOperation(value = "获取所有互评信息")
    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public Result<?> getAllComments(@StudentInfo User student) {
        return commentService.getComments(student.getId());
    }

    @ApiOperation(value = "获取互评任务")
    @RequestMapping(value = "/comment/task", method = RequestMethod.GET)
    public Result<?> getCommentTasks(@StudentInfo User student) {
        return commentService.pushCommentTasks(student.getId());
    }

    @ApiOperation(value = "检索同学")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Result<?> getPossibleStudents(@StudentInfo User student,
                                         @RequestParam(value = "class", required = false) @ApiParam("班级号") String sameClass,
                                         @RequestParam(value = "name",required = false) @ApiParam("姓名") String name,
                                         @RequestParam(value = "id",required = false) @ApiParam("学号") String userId,
                                         @RequestParam(value = "major", required = false) @ApiParam("专业") String major) {
        return studentInfoService.getPossibleStudent(student.getId(), sameClass, name, userId, major);
    }

    @ApiOperation(value = "强制更新某个班级需要互评的人数")
    @RequestMapping(value = "/comment/sync", method = RequestMethod.GET)
    public Result<?> forceSyncCommentTaskList(@RequestParam("classId") @ApiParam("要更新的班级id") String classId) {
        commentService.updateCommentTaskList(classId);
        return new Result<String>().fail();
    }
}
