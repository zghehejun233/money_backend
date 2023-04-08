package site.surui.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.common.data.po.Teacher;

import java.util.List;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    @Select("SELECT * FROM teacher WHERE id=#{id}")
    Teacher findTeacherById(@Param("id") Long id);

    @Select("SELECT `name` FROM teacher WHERE user_id=#{id}")
    String getTeacherName(@Param("id") String userId);

    @Select("SELECT * FROM teacher WHERE user_id=#{id}")
    Teacher getTeacherByUserId(@Param("id") String userId);

    @Select("SELECT id FROM teacher WHERE user_id=#{id}")
    Long getTeacherId(@Param("id") String userId);

    @Select("SELECT c.name FROM course c JOIN teacher t on t.id = c.tid WHERE t.id=#{id} AND c.status=1")
    List<String> getCourseNamesByTeacher(@Param("id") Long id);
}
