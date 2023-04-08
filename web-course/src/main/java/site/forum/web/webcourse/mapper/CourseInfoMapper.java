package site.forum.web.webcourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.common.data.po.CourseInfo;
import site.forum.web.common.data.po.CourseTime;
import site.forum.web.common.data.vo.StudentWithScore;

import java.util.List;

@Mapper
public interface CourseInfoMapper extends BaseMapper<CourseInfo> {

    List<CourseInfo> findAllByCondition(@Param("name") String name,
                                        @Param("teacher") String teacher,
                                        @Param("teacherId") Long teacherId,
                                        @Param("courseId") String courseId);

    @Select("SELECT id FROM student WHERE user_id=#{user_id}")
    Long findIdByStudentId(@Param("user_id") String userId);

    @Select("SELECT s.*,s2.* FROM student_course_relation relation JOIN student s on s.id = relation.student_id LEFT JOIN score s2 on s.id = s2.sid AND relation.course_id = s2.cid WHERE relation.course_id=#{course}")
    List<StudentWithScore> selectAllStudentByCourse(@Param("course") Long courseId);

    @Select("SELECT COUNT(*) FROM student_course_relation WHERE course_id=#{id}")
    int countByCourseId(@Param("id") Long courseId);


    List<CourseTime> findAllTimes(@Param("course") Long courseId,
                                  @Param("week") Integer week,
                                  @Param("start") Integer startWeek,
                                  @Param("end") Integer endWeek);

    List<CourseInfo> findCourseByNameAndCredit(@Param("name") String name,
                                               @Param("type") String type,
                                               @Param("state") Integer state,
                                               @Param("low") Integer low,
                                               @Param("high") Integer high);

    @Select("SELECT state FROM student_education_plan_relation WHERE sid=#{s} AND pid=#{p}")
    Integer getState(@Param("s") Long studentId, @Param("p") Long planId);

    @Select("SELECT e.type FROM education_plan e WHERE e.id=#{id}")
    String getType(@Param("id") Long planId);

    @Select("SELECT t.name FROM teacher t WHERE t.id=#{id}")
    String getTeacherName(@Param("id") Long teacherId);
}
