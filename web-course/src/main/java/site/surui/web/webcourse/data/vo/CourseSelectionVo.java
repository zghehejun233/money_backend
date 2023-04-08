package site.surui.web.webcourse.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSelectionVo {
    private Long id;
    private String courseId;
    private String name;
    private String teacher;
    private Float credit;
    private String type;
    private String location;
    private Integer capacity;
    private Integer selected;
    private Boolean isSelected;
    private Map<String, List<Integer>> time;
}
