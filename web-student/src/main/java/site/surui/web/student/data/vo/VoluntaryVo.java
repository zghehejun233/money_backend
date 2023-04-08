package site.surui.web.student.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoluntaryVo {
    private Long id;
    private String name;
    private String beginTime;
    private String endTime;
    private Float score;
    private Integer status;
    // 详细描述
    private String detail;

    // 参与人员
    private List<String> participants;
    // 服务机构
    private String serviceAgency;
    // 组织/负责人
    private String organizer;
    // 志愿时长
    private String duration;
}
