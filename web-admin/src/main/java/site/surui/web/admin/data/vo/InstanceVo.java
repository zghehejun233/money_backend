package site.surui.web.admin.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstanceVo {
    private String instanceId;
    private String ip;
    private Integer port;
    private String status;
    private String weight;
}
