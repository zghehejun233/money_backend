package site.forum.web.common.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学号或工号
     */
    private String userId;
    @TableField("pwd")
    private String password;
    private String email;
    @TableField("r")
    private int role;

}
