package site.surui.web.student.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.surui.web.common.data.event.PublishNotificationEvent;
import site.surui.web.common.util.NotificationUtil;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class PublishNotificationEventListener  {

    @Async
    @EventListener(classes = {PublishNotificationEvent.class})
    @Order(10)
    public void onApplicationEvent(@Nonnull PublishNotificationEvent event) {
        log.info("Notification event received.");
        try {
            NotificationUtil.addNotification(event.notification);
        } catch (Exception e) {
            log.warn("Notification event failed.", e);
            return;
        }
        log.info("Notification event processed.");
    }
}
