package site.forum.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.dto.ClassDto;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("SELECT * FROM student WHERE user_id=#{userId}")
    Student getStudentInfo(@Param("userId") String userId);

    @Select("SELECT * FROM student WHERE id=#{userId}")
    Student getStudentInfo(@Param("userId") Long id);

    @Update("UPDATE student SET avatar=#{url} WHERE user_id=#{id}")
    int updateAvatar(@Param("id")String userId, @Param("url")String url);

    @Select("SELECT * FROM student WHERE user_id=#{id}")
    Student findByUserId(@Param("id")String userId);

    @Select("SELECT class_info.* FROM class_info JOIN student s on class_info.id = s.class_id WHERE s.id=#{id}")
    ClassDto selectClassNameByStudent(@Param("id") Long studentId);

    @Select("SELECT s.avatar FROM student s WHERE id=#{id}")
    String getAvatar(@Param("id") Long studentId);

    @Select("SELECT id FROM student WHERE user_id=#{id}")
    Long getIdByUserId(@Param("id") String userId);

    @Select("SELECT s.name FROM student s WHERE id=#{id}")
    String getStudentName(@Param("id") Long studentId);

    List<Student> selectPossibleStudents(@Param("id") Long id,
                                         @Param("same") String sameClass,
                                         @Param("name") String name,
                                         @Param("userId") String userId,
                                         @Param("major") String major);

    List<Student> selectStudentsForSocialActivity(@Param("name") String name, @Param("id") String userId);
}
