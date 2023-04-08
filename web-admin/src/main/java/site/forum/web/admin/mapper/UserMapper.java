package site.forum.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.po.Teacher;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Delete("delete from user_info where user_id = #{userId}")
    int deleteUser(String userId);

    @Select("select * from student")
    List<Student> selectAllStudent();

    @Select("SELECT * FROM student WHERE user_id = #{id}")
    Student selectStudentById(String id);

    @Delete("DELETE FROM student WHERE id = #{id}")
    int deleteStudentById(Long id);

    @Select("SELECT * FROM teacher")
    List<Teacher> selectAllTeacher();

    @Select("SELECT * FROM teacher WHERE user_id = #{id}")
    Teacher selectTeacherById(String id);

    @Delete("DELETE FROM teacher WHERE id = #{id}")
    int deleteTeacherById(Long id);
}
