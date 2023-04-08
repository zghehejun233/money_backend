package site.forum.web.teacher.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收录论文成果
 * <p>
 * 字段设计参考GB／T7714-2015
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("论文成果")
public class Paper {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("论文标题")
    private String name;
    @ApiModelProperty(value = "论文类型", notes = "包括专著、连续出版物、析出文献、专利文献和电子文献")
    private String type;
    @ApiModelProperty(value = "发表信息", notes = "对应图书的出版信息或者论文的保存单位等信息")
    private String metaInfo;
    @ApiModelProperty(value = "发表年份")
    private String year;
    @ApiModelProperty(value = "资源地址", notes = "电子资源的URL或者图书的页数")
    private String source;
    private Long tid;
}
