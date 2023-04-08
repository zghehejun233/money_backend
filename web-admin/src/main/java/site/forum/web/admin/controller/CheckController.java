package site.forum.web.admin.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.forum.web.admin.service.ActivityService;
import site.forum.web.admin.service.CheckService;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("/check")
public class CheckController {

    @Resource
    CheckService checkService;
    @Resource
    private ActivityService activityService;

    @ApiOperation(value = "获取所有待审核课程")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getCheckingCourse() {
        return checkService.getCheckingCourse();
    }

    @ApiOperation(value = "过审一门课程")
    @RequestMapping(value = "/course", method = RequestMethod.PUT)
    public Result<?> passCourse(@RequestParam("id") Long id) {
        return checkService.passCourse(id);
    }

    @ApiOperation(value = "不过审一门课程")
    @RequestMapping(value = "/course", method = RequestMethod.DELETE)
    public Result<?> refuseCourse(@RequestParam("id") Long id) {
        return checkService.refuseCourse(id);
    }

    @ApiOperation(value = "获取所有待审核考试")
    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public Result<?> getCheckingExam() {
        return checkService.getCheckingExam();
    }

    @ApiOperation(value = "过审一次考试")
    @RequestMapping(value = "/exam", method = RequestMethod.PUT)
    public Result<?> passExam(@RequestParam("id") Long id) {
        return checkService.passExam(id);
    }

    @ApiOperation(value = "不过审一次考试")
    @RequestMapping(value = "/exam", method = RequestMethod.DELETE)
    public Result<?> refuseExam(@RequestParam("id") Long id) {
        return checkService.refuseExam(id);
    }

    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public Result<?> getActivity() {
        return checkService.getCheckingActivity();
    }

    @ApiOperation(value = "过审一条学生活动")
    @RequestMapping(value = "/activity", method = RequestMethod.PUT)
    public Result<?> passActivity(@RequestParam("id") Long activityId,
                                  @RequestParam("score") Float score) {
        return checkService.passActivity(activityId, score);
    }

    @ApiOperation(value = "不过审一条学生活动")
    @RequestMapping(value = "/activity", method = RequestMethod.DELETE)
    public Result<?> deleteActivity(@RequestParam("id") Long activityId) {
        return checkService.refuseActivity(activityId);
    }


}
