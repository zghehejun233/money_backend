package site.forum.web.student.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionVo {
    private Long id;
    private String name;
    private String time;
    // 评分
    private Float score;
    // 详细描述
    private String detail;

    // 获奖等级
    private String awardLevel;
    // 获奖单位
    private String awardUnit;
    // 获奖证书
    private String awardCertificate;
    private String status;
}
