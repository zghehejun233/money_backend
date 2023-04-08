package site.surui.web.teacher.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseNotificationVo<T> {
    private String message;
    private Integer status;
    private T payload;
}
