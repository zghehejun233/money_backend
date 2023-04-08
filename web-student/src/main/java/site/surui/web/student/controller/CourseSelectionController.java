package site.surui.web.student.controller;

import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.student.http.StudentInfo;
import site.surui.web.student.service.RemoteCourseService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/select")
@Slf4j
public class CourseSelectionController {
    @Resource
    private RemoteCourseService remoteCourseService;

    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getSelectingCourse(@StudentInfo User user,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "type", required = false) String type,
                                        @RequestParam(value = "low", required = false) Integer low,
                                        @RequestParam(value = "high", required = false) Integer high) {
        try {
            return remoteCourseService.getPossibleCourses(user.getId(), name, type, null, low, high);
        } catch (DecodeException e) {
            log.error("DecodeException: {}", e.getMessage());
            return new Result<>().fail(e.getMessage());
        }

    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Result<?> quitCourse(@StudentInfo User user,
                                @RequestParam("course") Long courseId) {
        return remoteCourseService.quitCourse(courseId, user.getId());
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Result<?> selectCourse(@StudentInfo User user,
                                  @RequestParam("course") Long courseId) {

        return remoteCourseService.selectCourse(courseId, user.getId());
    }
}
