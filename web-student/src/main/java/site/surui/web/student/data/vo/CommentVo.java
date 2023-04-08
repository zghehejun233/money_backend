package site.surui.web.student.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private String title;
    private String name;
    private String content;
    private String avatar;
    private String date;
}
