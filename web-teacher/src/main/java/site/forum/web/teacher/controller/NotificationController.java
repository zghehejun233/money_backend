package site.forum.web.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.forum.web.teacher.data.annotations.TeacherInfo;
import site.forum.web.teacher.service.NotificationService;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.Result;

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
