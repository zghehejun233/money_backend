package site.surui.web.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.surui.web.admin.service.NotificationService;
import site.surui.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("/notification")
@Slf4j
public class NotificationController {
    @Resource
    private NotificationService notificationService;
    @RequestMapping("/")
    public Result<?> getNotifications(@RequestParam("limit") int limit) {
        return notificationService.getNotifications(null, limit);
    }
}
