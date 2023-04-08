package site.forum.web.admin.data.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("活动实体类，用于管理员显示")
public class ActivityVo {
    private Long id;
    private String name;
    private String maintainer;
    private String type;
    private String status;
}
