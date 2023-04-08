package site.surui.web.teacher.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamVo {
    private String name;
    private String date;
    private String betime;
    private String endtime;
    private String area;
}
