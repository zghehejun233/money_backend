package site.forum.web.common.data.dto;


import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import site.forum.web.common.util.Base64Util;

import java.util.Date;

/**
 * 该类用于保存通用的通知信息
 */
@Setter
@Getter
public class Notification {
    // 通知的接收者类型, 具体定义在`ReverTypeEnum`中
    private int receiverType;
    // 通知的发送者的id
    private Long senderId;
    // 通知的接收者的id
    private Long receiverId;
    // 需要执行的操作类型
    private int actionType;
    // 通知的简介
    private String message;
    // 通知的创建时间通知的payload，借助action划分动作
    // 可以保存JSON序列化并BASE64编码后的字符串
    private String payload;
    // 通知的创建时间
    private Date timestamp;

    public Notification(int receiverType, Long receiverId, Long senderId, int actionType, String message, String payload) {
        this.receiverType = receiverType;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.actionType = actionType;
        this.message = message;
        this.payload = payload;
        this.timestamp = new Date();
    }

    public static Notification buildNotificationForStudent(Long receiverId, Long senderId, int actionType, String message, String payload) {
        return new Notification(ReceiverTypeEnum.STUDENT.getType(), receiverId, senderId, actionType, message, payload);
    }

    public static Notification buildNotificationForTeacher(Long receiverId, Long senderId, int actionType, String message, String payload) {
        return new Notification(ReceiverTypeEnum.TEACHER.getType(), receiverId, senderId, actionType, message, payload);
    }

    public static Notification buildNotificationForAdmin(Long receiverId, Long senderId, int actionType, String message, String payload) {
        return new Notification(ReceiverTypeEnum.ADMIN.getType(), receiverId, senderId, actionType, message, payload);
    }

    public static String toJson(Notification notification) {
        return JSONObject.toJSONString(notification);
    }

    public static Notification fromJson(String json) {
        return JSONObject.parseObject(json, Notification.class);
    }

    public static String toBase64Json(Notification notification) {
        return Base64Util.encode(toJson(notification));
    }

    public static Notification fromBase64Json(String base64Json) {
        return fromJson(Base64Util.decode(base64Json));
    }


}
