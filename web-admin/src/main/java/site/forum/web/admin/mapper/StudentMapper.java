package site.forum.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.common.data.dto.ClassDto;
import site.forum.web.common.data.po.Student;

import java.util.List;


@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    @Select("SELECT * FROM student WHERE id = #{id}")
    Student selectStudentById(Long id);

    @Select("SELECT * FROM class_info")
    List<ClassDto> selectAllClass();

    @Insert("INSERT INTO class_info (name,grade) VALUES (#{className},#{grade})")
    void insertNewClass(@Param("className") String className, @Param("grade") String grade);

    @Select("SELECT class_info.* FROM class_info JOIN student s on class_info.id = s.class_id WHERE s.id=#{id}")
    ClassDto selectClassNameByStudent(@Param("id") Long studentId);
}
