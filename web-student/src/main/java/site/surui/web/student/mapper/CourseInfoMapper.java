package site.surui.web.student.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.po.CourseTime;

import java.util.List;

@Mapper
public interface CourseInfoMapper extends BaseMapper<CourseInfo> {

    List<CourseInfo> findAllByCondition(@Param("name") String name,
                                        @Param("teacher") String teacher,
                                        @Param("teacherId") Long teacherId,
                                        @Param("courseId") String courseId);


    List<CourseTime> findAllTimes(@Param("course") Long courseId,
                                  @Param("week") Integer week,
                                  @Param("start") Integer startWeek,
                                  @Param("end") Integer endWeek);

}
