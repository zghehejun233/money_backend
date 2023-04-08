package site.surui.web.student.data.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
@Api("互评内容")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("被评论id")
    private Long studentId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty(value = "内容", notes = "使用longtext存储，支持长文本")
    private String content;
    private String date;
    @ApiModelProperty("评论者id")
    private Long sid;
}
