package site.forum.web.teacher.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamStatusVo {
    private Integer id;
    private String name;
    private String date;
    private String betime;
    private String endtime;
    private String area;
    private Long courseId;
    private Integer status;
}
