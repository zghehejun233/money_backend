package site.forum.web.student.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentWorkVo {
    private Long id;
    private String name;
    private String beginTime;
    private String endTime;
    private String detail;
    private Float score;
    private Integer status;

    // 职位
    private String position;
    // 部门
    private String department;
}
