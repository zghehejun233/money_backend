package site.surui.web.webcourse.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("student_course_relation")
public class SelectionDto {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long courseId;
    private Long studentId;
}
