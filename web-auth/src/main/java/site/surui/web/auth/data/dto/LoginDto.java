package site.surui.web.auth.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户登录信息", description = "保存登录ID和密码")
public class LoginDto {
    @ApiModelProperty(value = "学工号", required = true)
    private String userId;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
