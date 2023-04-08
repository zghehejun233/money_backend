package site.forum.web.student.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialPracticeVo {
    private String name;
    private String beginTime;
    private String endTime;
    private String detail;
    @ApiModelProperty("评分")
    private Float score;
    private Integer status;
    private Long maintainer;
    private String socialType;
    // 参与人员
    private List<String> participants;
}
