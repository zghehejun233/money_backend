package site.forum.web.teacher.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTimeDto {
    private List<Integer> weeks;
    private List<Integer> weekday;
    private List<Integer> order;
}
