package site.surui.web.webcourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.common.data.po.Period;
import site.surui.web.webcourse.data.dto.SelectionDto;

import java.util.List;

@Mapper
public interface SelectionMapper extends BaseMapper<SelectionDto> {

    @Select("SELECT student_course_relation.id FROM student_course_relation WHERE course_id=#{course} AND student_id=#{student} LIMIT 1")
    Long isSelected(@Param("course") Long courseId, @Param("student") Long student);

    @Select("INSERT INTO student_course_relation (course_id, id, student_id) VALUES (#{course}, null, #{student})")
    void insertSelection(@Param("course") Long courseId, @Param("student") Long student);

    @Select("DELETE FROM student_course_relation WHERE id=#{id}")
    int deleteSelection(@Param("id") Long id);

    @Select("SELECT * FROM student_course_relation WHERE course_id=#{course} AND student_id=#{student}")
    List<SelectionDto> findByCourseIdAndStudentId(@Param("course") Long courseId, @Param("student") Long studentId);

    @Select("SELECT * FROM period WHERE end = (SELECT MAX(end) FROM period)")
    Period getLastPeriod();
}
