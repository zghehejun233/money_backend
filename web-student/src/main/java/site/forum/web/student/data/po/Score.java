package site.forum.web.student.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@Api("成绩实体类")
@TableName("score")
public class Score {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "平时分", notes = "统一使用百分制计算，在service层重新封装变换")
    private Float usual;
    private Float exam;
    private Float total;
    private boolean included;
    @TableField("cid")
    private Long cid;
    @TableField("sid")
    private Long sid;
    private Integer rank;
    private boolean isLock;
}