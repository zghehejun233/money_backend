package site.forum.web.student.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.forum.web.student.service.ActivityService;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@RestController
@Api("用于学生获取综合素养信息")
@RequestMapping("/activity")
public class BaseActivityController {
    @Resource
    private ActivityService activityService;


    @ApiOperation(value = "搜索可能的成员")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Result<?> searchMember(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "id", required = false) String userId) {
        return activityService.searchStudents(name, userId);
    }
}
