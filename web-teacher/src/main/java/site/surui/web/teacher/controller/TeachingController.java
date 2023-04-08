package site.surui.web.teacher.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.teacher.data.annotations.TeacherInfo;
import site.surui.web.teacher.data.dto.CourseTimeDto;
import site.surui.web.common.data.po.Score;
import site.surui.web.teacher.data.vo.ExamVo;
import site.surui.web.teacher.service.CourseService;
import site.surui.web.teacher.service.TeachingService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("教学信息接口")
@RequestMapping("/teach")
public class TeachingController {
    @Resource
    private TeachingService teachingService;
    @Resource
    private CourseService courseService;

    @ApiOperation(value = "获取自己开设的课程", notes = "已测试")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getCourses(@TeacherInfo @ApiParam(hidden = true) User user) {
        return teachingService.findCourses(user.getId());
    }

    @ApiOperation(value = "增加一门课程", notes = "已测试")
    @RequestMapping(value = "/course", method = RequestMethod.PUT)
    public Result<?> addCourse(@TeacherInfo @ApiParam(hidden = true) User user,
                               @RequestBody @ApiParam("课程信息") CourseInfo courseInfo) {
        return teachingService.addCourse(user.getId(), courseInfo);
    }

    @ApiOperation(value = "添加一门课程的时间", notes = "已测试")
    @RequestMapping(value = "/course/time", method = RequestMethod.PUT)
    public Result<?> addCourseTime(@RequestParam("course") @ApiParam("课程id") Long courseId,
                                   @RequestBody @ApiParam("课程时间") CourseTimeDto courseTime) {
        return teachingService.addCourseTime(courseId, courseTime);
    }

    @ApiOperation(value = "删除一门课程的上课时间")
    @RequestMapping(value = "/course/time", method = RequestMethod.DELETE)
    public Result<?> deleteCourseTime(@RequestParam("course") @ApiParam("课程id") Long courseId,
                                      @RequestBody @ApiParam("课程时间") CourseTimeDto courseTime) {
        return teachingService.deleteCourseTime(courseId, courseTime);
    }

    @ApiOperation("删除一门课程")
    @RequestMapping(value = "/course", method = RequestMethod.DELETE)
    public Result<?> deleteCourse(@TeacherInfo @ApiParam(hidden = true) User user,
                                  @RequestParam("id") @ApiParam("课程id") Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @ApiOperation(value = "修改某门课程的课容量", notes = "已测试")
    @RequestMapping(value = "/course/capacity", method = RequestMethod.POST)
    public Result<?> capacityCourse(@RequestParam("id") @ApiParam(value = "课程id", required = true) Long courseId,
                                    @RequestParam("num") @ApiParam(value = "要扩增到的人数", required = true) Integer num) {
        return teachingService.enrichCapacity(courseId, num);
    }

    @ApiOperation(value = "获取一门课程的学生名单", notes = "已测试")
    @RequestMapping(value = "/course/student", method = RequestMethod.GET)
    public Result<?> getStudentList(@RequestParam("id") @ApiParam(value = "课程号", required = true) Long courseId) {
        return teachingService.getStudentList(courseId);
    }

    @ApiOperation(value = "设置成绩")
    @RequestMapping(value = "/score", method = RequestMethod.PUT)
    public Result<?> setScore(@RequestParam("course") Long courseId, @RequestBody List<Score> scores) {
        return teachingService.setScores(courseId, scores);
    }

    @ApiOperation(value = "设置一次考试")
    @RequestMapping(value = "/exam", method = RequestMethod.PUT)
    public Result<?> putExam(@TeacherInfo User user,
                             @RequestParam("course") Long courseId,
                             @RequestBody ExamVo exam) {
        return teachingService.addExam(courseId, user.getId(), exam);
    }

    @ApiOperation("查询考试信息")
    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public Result<?> getExams(@TeacherInfo User user,
                              @RequestParam(value = "course", required = false) Long courseId) {
        return (courseId == null) ?
                teachingService.getExams(user.getId()) : teachingService.getExamStudents(courseId);
    }

    @ApiOperation("将学生添加到考试中")
    @RequestMapping(value = "/exam", method = RequestMethod.POST)
    public Result<?> addStudentsToExam(@TeacherInfo User user,
                                       @RequestParam("course") Long courseId,
                                       @RequestBody List<Long> studentIds) {
        return teachingService.addStudentExamRelation(user.getId(), courseId,studentIds);
    }
    @ApiOperation("获取自己申请的考试及状态")
    @RequestMapping(value = "/examstatus", method = RequestMethod.GET)
    public Result<?> getExamStatus(@TeacherInfo User user) {
        return teachingService.getExamStatusById(user.getId());
    }

    @ApiOperation(value = "获取课程表")
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public Result<?> getSchedule(@TeacherInfo User user,
                                 @RequestParam(value = "start", required = false) Integer start,
                                 @RequestParam(value = "end", required = false) Integer end) {
        return teachingService.getSchedule(user.getId(), start, end);
    }
}
