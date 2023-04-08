package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.student.data.po.EducationPlan;

import java.util.List;

@Mapper
public interface EducationPlanMapper extends BaseMapper<EducationPlan> {

    List<EducationPlan> findAllByStudent(@Param("id") Long studentId,
                                         @Param("name") String name,
                                         @Param("type") String type,
                                         @Param("state") Integer state,
                                         @Param("high") Integer high,
                                         @Param("low") Integer low);

    @Select("SELECT state FROM student_education_plan_relation WHERE sid=#{s} AND pid=#{p}")
    Integer getState(@Param("s") Long studentId, @Param("p") Long planId);

    @Select("SELECT e.type FROM education_plan e WHERE e.id=#{id}")
    String getType(@Param("id") Long planId);
}
