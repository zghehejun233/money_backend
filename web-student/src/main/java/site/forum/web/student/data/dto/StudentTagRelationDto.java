package site.forum.web.student.data.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("student_tag_relation")
public class StudentTagRelationDto {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("sid")
    private String userId;
    @TableField("tag_id")
    private Long tagId;
    private Integer weight;
}
