package site.forum.web.student.data.po;

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
@TableName("class_info")
@ApiModel("班级信息")
public class ClassInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("班级名称")
    private String name;
    @ApiModelProperty("年级")
    private String grade;
}
