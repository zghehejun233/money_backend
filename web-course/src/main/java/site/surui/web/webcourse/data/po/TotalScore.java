package site.surui.web.webcourse.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
