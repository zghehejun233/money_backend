package site.forum.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import site.forum.web.common.data.po.CourseTime;

@Mapper
public interface CourseTimeMapper extends BaseMapper<CourseTime> {
    @Select("SELECT * FROM course_time WHERE week=#{week} AND day=#{weekday} AND `order`=#{order} LIMIT 1")
    CourseTime findCourseTimeByDetail(@Param("week") Integer week,
                                      @Param("weekday") Integer weekday,
                                      @Param("order") Integer order);

    @Delete("DELETE FROM course_time_relation WHERE cid=#{courseId}")
    void deleteRelatedCourseByCourseId(@Param("courseId") Long courseId);

    @Insert("INSERT INTO course_time (week, day, `order`) VALUES (#{week}, #{weekday}, #{order})")
    void addCourseTime(@Param("week") Integer week,
                       @Param("weekday") Integer weekday,
                       @Param("order") Integer order);
    @Select("SELECT * FROM course_time_relation WHERE cid=#{cid} AND tid=#{tid}")
    Object findRelation(@Param("cid") Long courseId, @Param("tid") Long timeId);
    @Insert("INSERT INTO course_time_relation (cid,tid) VALUES (#{cid},#{tid})")
    void addCourseRelation(@Param("cid") Long courseId, @Param("tid") Long teacherId);

    @Delete("DELETE FROM course_time_relation WHERE cid=#{cid} AND tid=#{tid}")
    void deleteRelatedCourseByTimeId(@Param("cid") Long courseId, @Param("tid") Long timeId);
}
