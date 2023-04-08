package site.surui.web.student.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogVo {
    private Long id;
    private String title;
    private String content;
    private String name;
    private String time;
    private String avatar;
}