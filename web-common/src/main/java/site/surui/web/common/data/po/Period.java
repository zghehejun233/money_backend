package site.surui.web.common.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("period")
public class Period {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String begin;
    private String end;
}
