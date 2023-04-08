package site.forum.web.common.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("学生实体信息")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("学号")
    private String userId;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty(value = "出厂日期", notes = "需要一个标准日期对象")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @ApiModelProperty(value = "性别", notes = "使用枚举类实现")
    private String gender;
    @ApiModelProperty("班级")
    private String className;
    @ApiModelProperty(value = "年级", notes = "建议统一使用\"级\"")
    private String grade;
    @ApiModelProperty("主修专业")
    private String major;
    @ApiModelProperty(value = "头像", notes = "传输一个比特流")
    private String avatar;
    @ApiModelProperty("地区编码")
    private Long area;
    private String classId;
}
