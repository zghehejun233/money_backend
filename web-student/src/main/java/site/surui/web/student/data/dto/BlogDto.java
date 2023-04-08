package site.surui.web.student.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "博客", description = "包含标题及内容")
public class BlogDto {
    @ApiModelProperty(value = "标题",required = true)
    private String title;
    @ApiModelProperty(value = "内容",required = true)
    private String content;
}