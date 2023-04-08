package site.surui.web.admin.data.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamVo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String date;
    private String beginTime;
    private String endTime;
    private String area;
    private Long tid;
    private Long cid;
    private Integer status;
    private String teacher;
    private String courseId;
}
