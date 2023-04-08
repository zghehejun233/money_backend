package site.forum.web.common.util;

import cn.hutool.db.nosql.redis.RedisDS;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import site.forum.web.common.data.dto.Notification;

import java.util.List;

@Slf4j
public class NotificationUtil {
    private static final String STUDENT_CHANNEL = "notification:student";
    private static final String TEACHER_CHANNEL = "notification:teacher";
    private static final String ADMIN_CHANNEL = "notification:admin";

    public static void addNotification(Notification notification) {
        log.info("{} will be inserted.", notification);
        String parsedNotification = Notification.toBase64Json(notification);
        log.info("Parsed notification: {}", parsedNotification);
        Jedis jedis = RedisDS.create().getJedis();
        switch (notification.getReceiverType()) {
            case 0:
                log.info("Student notification will be inserted.");
                jedis.lpush(STUDENT_CHANNEL, parsedNotification);
                log.info("Student notification inserted.");
                break;
            case 1:
                log.info("Teacher notification will be inserted.");
                jedis.lpush(TEACHER_CHANNEL, parsedNotification);
                log.info("Teacher notification inserted.");
                break;
            case 2:
                log.info("Admin notification will be inserted.");
                jedis.lpush(ADMIN_CHANNEL, parsedNotification);
                log.info("Admin notification inserted.");
                break;
            default:
                log.info("Unknown notification type.");
                break;
        }
    }

    public static <T> List<String> getNotifications(int type, int limit) {
        Jedis jedis = RedisDS.create().getJedis();
        List<String> parsedNotifications;
        switch (type) {
            case 0:
                parsedNotifications = jedis.lrange(STUDENT_CHANNEL, 0, limit);
                break;
            case 1:
                parsedNotifications = jedis.lrange(TEACHER_CHANNEL, 0, limit);
                break;
            case 2:
                parsedNotifications = jedis.lrange(ADMIN_CHANNEL, 0, limit);
                break;
            default:
                log.warn("Unknown notification type.");
                return null;
        }
        jedis.close();

        return parsedNotifications;
        // List<Notification> notifications = new ArrayList<>();
        // parsedNotifications
        //         .forEach(parsedNotification -> {
        //             Notification notification = Notification.fromBase64Json(parsedNotification);
        //             log.info("Decoded notification: {}", notification);
        //             notifications.add(notification);
        //         });
        // if (id != null) {
        //     notifications
        //             .stream()
        //             .filter(notification -> notification.getReceiverId() != null && !notification.getReceiverId().equals(id))
        //             .forEach(notification -> {
        //                 log.info("Notification {} will be removed.", notification);
        //                 notifications.remove(notification);
        //             });
        // }
        // return notifications;
    }
}
