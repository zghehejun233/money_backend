package site.surui.web.webcourse.controller;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.webcourse.service.SelectionService;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@Api("选课中心")
@RequestMapping("/select")
@Slf4j
public class SelectionController {
    @Resource
    private SelectionService selectionService;

    @ApiOperation(value = "设置选课时间", notes = "已测试")
    @RequestMapping(value = "/time", method = RequestMethod.POST)
    public Result<?> setSelectTime(@RequestParam("id") @ApiParam("课程的id") Long id,
                                   @RequestParam("start") @ApiParam("开始时间") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                   @RequestParam("end") @ApiParam("结束时间") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return selectionService.setSelectionTime(id, start, end);
    }

    @ApiOperation(value = "获取选课学生名单", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Result<?> getStudentList(@RequestParam("id") @ApiParam("课程ID") Long id) {
        return selectionService.findAllStudentByCourse(id);
    }

    @ApiOperation(value = "学生选择课程", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Result<?> selectCourse(@RequestParam("course") @ApiParam("课程ID") Long courseId,
                                  @RequestParam("student") @ApiParam("学生ID") Long studentId) {
        log.warn("Before service " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss:S"));
        return selectionService.selectCourse(courseId, studentId);
    }

    @ApiOperation(value = "学生退选", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Result<?> quitCourse(@RequestParam("course") @ApiParam("课程ID") Long courseId,
                                @RequestParam("student") @ApiParam("学生ID") Long studentId) {
        return selectionService.removeSelection(courseId, studentId);
    }

    @ApiOperation(value = "筛选可选课程", notes = "已测试")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getCourse(@RequestParam("id") Long studentId,
                               @RequestParam(value = "name", required = false) @ApiParam("课程名称") String name,
                               @RequestParam(value = "type", required = false) @ApiParam("课程类型") String type,
                               @RequestParam(value = "state", required = false) @ApiParam("学习状态") Integer state,
                               @RequestParam(value = "low", required = false) @ApiParam("最低学分") Integer low,
                               @RequestParam(value = "high", required = false) @ApiParam("最高学分") Integer high) {
        return selectionService.getPossibleCourse(studentId, name, type, state, low, high);
    }
}
