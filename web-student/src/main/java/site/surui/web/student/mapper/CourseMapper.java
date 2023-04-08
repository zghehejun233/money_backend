package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.common.data.po.CourseInfo;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<CourseInfo> {

    @Select("SELECT course.* FROM course JOIN student_course_relation scr on course.id = scr.course_id WHERE student_id = #{id} AND course.status=1")
    List<CourseInfo> findALlByStudent(@Param("id") Long userId);

    @Select("SELECT * FROM course WHERE course_id=#{id} AND course.status=1")
    CourseInfo findByCourseId(@Param("id") String courseId);

    @Select("SELECT * FROM course WHERE id=#{id} AND course.status=1")
    CourseInfo findById(@Param("id") Long courseId);
}
