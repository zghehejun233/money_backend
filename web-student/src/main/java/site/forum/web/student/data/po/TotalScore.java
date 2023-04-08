package site.forum.web.student.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("total_score")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalScore {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sid;
    private Integer totalRank;
    private Float totalScore;
}
