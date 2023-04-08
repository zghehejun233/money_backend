package site.forum.web.admin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemScaleInfoDto {
    private Integer studentScale;
    private Integer adminScale;
    private Integer teacherScale;
    private Date time;
}
