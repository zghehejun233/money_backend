package site.forum.web.student.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCommentVo {
    private Long id;
    private String name;
    private String userId;
    private String className;
    private String major;
    private String avatar;
}
