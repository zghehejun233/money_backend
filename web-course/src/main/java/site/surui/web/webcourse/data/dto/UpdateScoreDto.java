package site.surui.web.webcourse.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UpdateScoreDto {
    private Date time;
    private int count;
}
