package site.surui.web.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.teacher.data.annotations.TeacherInfo;
import site.surui.web.teacher.service.NotificationService;

import javax.annotation.Resource;

@RequestMapping("/notification")
@RestController
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getCourseNotification(@TeacherInfo User user, @RequestParam("status") Integer status) {
        return notificationService.getCourseNotification(status, user.getId());
    }

    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public Result<?> getExamNotification(@TeacherInfo User user, @RequestParam("status") Integer status) {
        return notificationService.getExamNotification(status, user.getId());
    }

}
