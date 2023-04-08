package site.forum.web.common.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("course_time")
@Api("课")
public class CourseTime {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("周数")
    private Integer week;
    @ApiModelProperty("周几")
    private Integer day;
    @ApiModelProperty("第几节课")
    private Integer order;
}

