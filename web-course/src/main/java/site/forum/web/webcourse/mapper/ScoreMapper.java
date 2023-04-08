package site.forum.web.webcourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import site.forum.web.webcourse.data.po.Score;

import java.util.List;

@Mapper
public interface ScoreMapper extends BaseMapper<Score> {
    @Select("SELECT s.* FROM score s JOIN student s2 on s2.id = s.sid WHERE s2.user_id=#{id}")
    List<Score> findAllStudentWithScore(@Param("id") String id);

    @Select("UPDATE score SET sid=#{student} WHERE score.id=#{id}")
    void setSid(@Param("id") Long id, @Param("student") Long studentId);

    @Select("SELECT s.* FROM score s WHERE s.cid=#{course}")
    List<Score> findAllByCid(@Param("course") Long courseId);

    @Select("SELECT s.* FROM score s WHERE s.sid=#{student}")
    List<Score> findALlBySid(@Param("student") Long studentId);

    @Update("UPDATE score SET `rank`=#{rank} WHERE id=#{id}")
    void updateRank(@Param("rank") Integer rank, @Param("id") Long id);
}
