package site.surui.web.common.data.event;

import org.springframework.context.ApplicationEvent;
import site.surui.web.common.data.dto.Notification;

public class PublishNotificationEvent extends ApplicationEvent {
    public Notification notification;

    public PublishNotificationEvent(Object source) {
        super(source);
    }
    public PublishNotificationEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }
}
