package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.common.data.po.Student;
import site.surui.web.student.data.po.Comment;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT * FROM comment WHERE student_id=#{id}")
    List<Comment> findAllComments(@Param("id") Long studentId);


    List<Student> findAllUnsatisfiedComments(@Param("id") Long studentId);

    @Select("SELECT COUNT(*) FROM comment WHERE student_id=#{id}")
    int countComments(@Param("id") Long studentId);
}
