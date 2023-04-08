package site.surui.web.webcourse.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SelectionEventDto {
    private Long studentId;
    private Long courseId;
}
