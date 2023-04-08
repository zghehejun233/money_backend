package site.forum.web.common.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVo {
    private String message;
    private String content;
    private int actionType;
    private Date timestamp;
}
