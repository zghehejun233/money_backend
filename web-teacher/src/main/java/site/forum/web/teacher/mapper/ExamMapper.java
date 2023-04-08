package site.forum.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.teacher.data.vo.ExamStatusVo;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.po.Exam;

import java.util.List;

@Mapper
public interface ExamMapper extends BaseMapper<Exam> {
    @Select("SELECT * FROM exam WHERE id=#{id} AND exam.status=1")
    List<Exam> selectByTeacher(@Param("id") Long teacherId);

    List<Student> selectStudentsByCourse(@Param("id") Long courseId);

    @Select("SELECT * FROM exam WHERE cid=#{course} AND tid=#{teacher} AND status=1 LIMIT 1")
    Exam selectExamByCourseAndTeacher(@Param("course") Long courseId, @Param("teacher") Long teacherId);

    @Insert("INSERT INTO student_exam_relation (sid, eid) VALUES (#{student}, #{exam})")
    void insertExamRelation(@Param("exam") Long examId, @Param("student") Long studentId);

    List<Exam> selectExamByStatusAndId(@Param("status") Integer status, @Param("tid") Long id);

    @Select("SELECT e.*, e.begin_time AS betime, c.id AS course_id FROM exam e JOIN course c on e.cid=c.id WHERE e.tid=#{tid} AND c.status=1")
    List<ExamStatusVo> selectExamStatusByTeacherId(@Param("tid") Long tid);
}
