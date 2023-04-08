package site.surui.web.webcourse.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.webcourse.service.CourseInfoService;

import javax.annotation.Resource;

@RestController
@Api("课程信息")
@RequestMapping("/course")
public class CourseInfoController {
    @Resource
    private CourseInfoService courseInfoService;

    @ApiOperation(value = "添加一门课程", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Result<?> getAllCourse(@RequestBody @ApiParam(value = "课程信息", required = true) CourseInfo courseInfo) {
        return courseInfoService.addCourse(courseInfo);
    }

    @ApiOperation(value = "删除一门课程", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Result<?> deleteCourse(@RequestParam("id") @ApiParam(value = "课程id(主键)", required = true) Long courseId) {
        return courseInfoService.deleteCourse(courseId);
    }

    @ApiOperation(value = "编辑课程信息", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Result<?> editCourse(@RequestParam("id") @ApiParam(value = "课程id", required = true) Long courseId,
                             @RequestBody @ApiParam(value = "需要修改的内容", required = true) JSONObject data) {
        return courseInfoService.editCourseInfo(courseId, data);
    }

    @ApiOperation(value = "搜索课程信息", notes = "暂时不支持使用教师工号搜索，其余功能正常")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Result<?> getCourseBy(@RequestParam(value = "name", required = false) @ApiParam("课程名称") String name,
                              @RequestParam(value = "teacher", required = false) @ApiParam("授课教师") String teacher,
                              @RequestParam(value = "teacherId", required = false) @ApiParam("授课教师id") Long teacherId,
                              @RequestParam(value = "id", required = false) @ApiParam("课程号") String courseId) {
        return courseInfoService.getCoursesByCondition(name, teacher, teacherId, courseId);
    }

    @ApiOperation("搜索所有选择某个课程的学生信息")
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public Result<?> getStudents(@RequestParam(value = "courseId") @ApiParam(value = "课程id", required = true) Long courseId) {
        return courseInfoService.getStudentByCourseId(courseId);
    }


    @ApiOperation(value = "获取课程时间")
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public Result<?> getCourseTime(@RequestParam("course") @ApiParam(value = "课程的id", required = true) Long courseId,
                                @RequestParam(value = "week", required = false) @ApiParam("必要时可以限制周数") Integer week,
                                @RequestParam(value = "start", required = false) @ApiParam("开始周") Integer startWeek,
                                @RequestParam(value = "end", required = false) @ApiParam("结束周") Integer endWeek) {
        return courseInfoService.getTimeByCourseAndWeek(courseId, week, startWeek, endWeek);
    }
}
