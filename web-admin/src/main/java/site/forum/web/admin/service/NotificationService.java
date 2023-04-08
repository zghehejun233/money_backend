package site.forum.web.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.forum.web.common.data.dto.Notification;
import site.forum.web.common.data.dto.ReceiverTypeEnum;
import site.forum.web.common.data.vo.NotificationVo;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.util.NotificationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NotificationService {

    public Result<?> getNotifications(Long id, int limit) {
        List<NotificationVo> vos = new ArrayList<>();
        Objects.requireNonNull(NotificationUtil.getNotifications(ReceiverTypeEnum.ADMIN.getType(), limit))
                .forEach(notification -> {
                    Notification n = Notification.fromBase64Json(notification);
                    if (n.getReceiverId() == null || (n.getReceiverId().equals(id))) {
                        NotificationVo vo = new NotificationVo(n.getMessage(), null, n.getActionType(), n.getTimestamp());
                        vos.add(vo);
                    }
                });
        return new Result<>().success(vos);
    }
}
