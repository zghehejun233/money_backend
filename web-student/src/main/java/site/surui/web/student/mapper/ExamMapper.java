package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.student.data.po.Exam;

import java.util.List;

@Mapper
public interface ExamMapper extends BaseMapper<Exam> {
    @Select("SELECT e.* FROM exam e INNER JOIN student_course_relation r on e.cid=r.course_id WHERE r.student_id=#{id} AND e.status=1")
    List<Exam> selectByStudent(@Param("id") Long studentId);
}
