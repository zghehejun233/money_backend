package site.forum.web.admin.mapper;

import org.apache.ibatis.annotations.*;
import site.forum.web.admin.data.vo.BaseActivityVo;
import site.forum.web.admin.data.vo.ExamVo;
import site.forum.web.common.data.po.CourseInfo;

import java.util.List;

@Mapper
public interface CheckMapper {

    @Select("SELECT * FROM course WHERE status=0")
    List<CourseInfo> selectCheckingCourse();

    @Update("UPDATE course SET status=1 WHERE id=#{id}")
    void setCoursePass(@Param("id") Long id);

    @Update("UPDATE course SET status=2 WHERE id=#{id}")
    void setCourseRefused(@Param("id") Long id);

    @Select("SELECT e.*,t.name as teacher,c.course_id FROM exam e JOIN teacher t on e.tid = t.id JOIN course c on c.id = e.cid WHERE e.status=0")
    List<ExamVo> selectCheckingExam();

    @Update("UPDATE exam SET status=1 WHERE id=#{id}")
    void setExamPass(@Param("id") Long id);

    @Update("UPDATE exam SET status=2 WHERE id=#{id}")
    void setExamRefused(@Param("id") Long id);

    @Select("SELECT a.*,s.name AS userName FROM activity a JOIN student s on a.maintainer = s.id WHERE a.status=0")
    List<BaseActivityVo> selectCheckingActivity();

    @Update("UPDATE activity SET status=1, score=#{score} WHERE id = #{id}")
    Integer setActivityPass(@Param("id") Long activityId, @Param("score") Float score);

    @Delete("UPDATE activity SET status=2 WHERE id = #{id}")
    Integer setActivityRefused(@Param("id") Long id);


}
