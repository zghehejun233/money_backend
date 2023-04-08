package site.forum.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.student.data.po.Score;

import java.util.List;

@Mapper
public interface ScoreMapper extends BaseMapper<ScoreMapper> {

    @Select("SELECT sc.* FROM score sc JOIN student s on s.id = sc.sid WHERE s.user_id=#{id}")
    List<Score> findScoresByStudentId(@Param("id") String userId);

    @Select("SELECT sc.* FROM score sc JOIN student s on s.id = sc.sid WHERE s.user_id=#{id} AND sc.cid=#{course}")
    Score findScoreByStudentAndCourse(@Param("id")String userId, @Param("course")Long courseId);
}
