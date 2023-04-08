package site.surui.web.common.data.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("综合素养活动实体类")
@TableName("activity")
public class BaseActivity {
    // Common
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty(value = "活动类型", notes = "例如社会实践、学生工作、志愿服务、科研竞赛")
    private Integer type;
    @ApiModelProperty("活动（开始）时间")
    private String time;
    private String beginTime;
    private String endTime;
    @ApiModelProperty("详细描述")
    private String detail;
    private Integer status;
    private Long maintainer;
    @ApiModelProperty("评分")
    private Float score;
    private String socialType;

    // StudentWork
    private String position;
    private String department;

    // VoluntaryService
    private String serviceAgency;
    private String organizer;
    private String duration;

    // Research
    private String articleTitle;
    private String publication;
    private String meta;
    private String url;

    // Competition
    private String awardLevel;
    private String awardUnit;
    private String awardCertificate;

}
