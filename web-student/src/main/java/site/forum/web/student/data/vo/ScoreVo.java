package site.forum.web.student.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("成绩VO")
public class ScoreVo {
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("课程号")
    private String number;
    @ApiModelProperty("平时分")
    private Float daily;
    @ApiModelProperty("考试分")
    private Float exam;
    @ApiModelProperty("总成绩")
    private Float total;
    @ApiModelProperty("排名")
    private Integer rank;
    @ApiModelProperty("学分")
    private Float weight;
}
