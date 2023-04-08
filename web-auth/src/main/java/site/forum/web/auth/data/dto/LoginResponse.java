package site.forum.web.auth.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(value = "登录返回值", description = "登录成功后返回")
public class LoginResponse {
    @ApiModelProperty("Token")
    private String token;
    @ApiModelProperty("权限")
    private int role;
    @ApiModelProperty("邮箱")
    private String email;
}
