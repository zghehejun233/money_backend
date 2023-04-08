package site.surui.web.student.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("学生博客")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("学生id关联")
    private Long sid;
    @ApiModelProperty("发布时间")
    private String time;
}
