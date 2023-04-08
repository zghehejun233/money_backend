package site.forum.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.common.data.po.CourseInfo;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<CourseInfo> {

    List<CourseInfo> selectCourseByStatusAndId(@Param("status") Integer status, @Param("tid") Long id);

    @Select("SELECT course.* FROM course  WHERE tid = #{id} AND course.status=1")
    List<CourseInfo> findALlByTeacherId(@Param("id") Long teacherId);

    @Select("SELECT * FROM course WHERE course_id=#{courseId}")
    CourseInfo findCourseByCourseId(@Param("courseId") String courseId);
}
