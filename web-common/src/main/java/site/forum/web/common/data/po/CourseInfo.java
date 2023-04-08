package site.forum.web.common.data.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程实体类信息")
@TableName("course")
public class CourseInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("课程号")
    private String courseId;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("老师")
    private String teacher;
    @ApiModelProperty("学分")
    private Float credit;
    @ApiModelProperty("课容量")
    private Integer capacity;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("开设学院")
    private String college;
    @ApiModelProperty("开始选课时间")
    private Date start;
    @ApiModelProperty("结束选课时间")
    private Date end;
    @ApiModelProperty("是否锁定")
    private Boolean isLock;
    @ApiModelProperty("教室")
    private String location;
    private Long tid;
    @ApiModelProperty("培养计划id")
    private Long pid;
    private Integer status;
}
