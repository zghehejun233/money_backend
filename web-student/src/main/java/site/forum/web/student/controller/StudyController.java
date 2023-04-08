package site.forum.web.student.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.forum.web.student.http.StudentInfo;
import site.forum.web.student.service.CourseService;
import site.forum.web.student.service.ScoreService;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@RestController
@Api("学生学业信息")
@RequestMapping("/study")
@RefreshScope
public class StudyController {

    @Resource
    private ScoreService scoreService;

    @Resource
    private CourseService courseService;

    @ApiOperation(value = "获取学生的所有成绩", notes = "---已测试---\n")
    @RequestMapping(value = "/score/all", method = RequestMethod.GET)
    public Result<?> getAllScores(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo) {
        return scoreService.getAllScores(userInfo.getUserId());
    }

    @ApiOperation(value = "获取学生的特定成绩", notes = "---已测试---\n")
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public Result<?> getScore(@StudentInfo @ApiParam(hidden = true) User userInfo,
                              @RequestParam("course") @ApiParam(value = "课程名id", required = true) String courseId) {
        return scoreService.getScore(userInfo.getUserId(), courseId);
    }


    @ApiOperation(value = "获取学生的所有课程", notes = "---已测试---\n")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getAllCourses(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo) {
        return courseService.getAllCourses(userInfo.getId());
    }

    @ApiOperation(value = "获取课程表")
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public Result<?> getSchedule(@StudentInfo User user,
                                 @RequestParam(value = "start", required = false) Integer start,
                                 @RequestParam(value = "end", required = false) Integer end) {
        return courseService.getSchedule(user.getId(), start, end);
    }

    @ApiOperation("获取考试信息")
    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public Result<?> getExams(@StudentInfo User user) {
        return courseService.getExams(user.getId());
    }
}
