package site.forum.web.webcourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import site.forum.web.webcourse.data.po.TotalScore;

import java.util.List;

@Mapper
public interface TotalScoreMapper extends BaseMapper<TotalScore> {

    @Select("SELECT * FROM total_score WHERE sid=#{id}")
    TotalScore selectBySid(@Param("id") Long studentId);

    @Select("SELECT * FROM total_score")
    List<TotalScore> selectAll();

    @Update("UPDATE total_score SET `total_rank`=#{rank} WHERE id=#{id}")
    void updateRank(@Param("rank") int rank, @Param("id") Long id);

    @Insert("INSERT INTO total_score (id, sid, total_score, total_rank) VALUES (null, #{sid}, #{score}, null)")
    void insertByHand(@Param("sid") Long sid, @Param("score") Float score);
}
