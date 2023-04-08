package site.surui.web.student.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("培养计划")
@TableName("education_plan")
public class EducationPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer number;
    private String apartment;
    private String openTime;
    private Integer weight;
    private String type;
    private Integer state;
    private Integer duration;
}
