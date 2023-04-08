package site.forum.web.common.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("教师实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("teacher")
public class Teacher {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("工号")
    private String userId;
    @ApiModelProperty("姓名")
    private String name;
    private String pinyin;
    @ApiModelProperty("性别")
    private String gender;
    @ApiModelProperty("职称")
    private String title;
    @ApiModelProperty("职位")
    private String duty;
    @ApiModelProperty("学院")
    private String college;
    @ApiModelProperty("自我介绍")
    private String introduction;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("研究方向")
    private String research;
    @ApiModelProperty(value = "讲授课程",notes = "使用\",\"分隔，前端自行解析吧")
    @TableField("course")
    private String lectureCourse;
    @ApiModelProperty("联系方式")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("办公室")
    private String office;
    @ApiModelProperty("学历")
    private String educationBackground;
    @ApiModelProperty("学位")
    private String degree;
    @ApiModelProperty("毕业院校")
    private String graduated;
    @ApiModelProperty("专业")
    private String major;
}
