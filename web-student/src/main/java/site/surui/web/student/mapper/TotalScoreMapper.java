package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.student.data.po.TotalScore;

@Mapper
public interface TotalScoreMapper extends BaseMapper<TotalScore> {

    @Select("SELECT ts.* FROM total_score ts JOIN student s on s.id = ts.sid WHERE s.user_id=#{id}")
    TotalScore selectAllByUserId(@Param("id") String userId);

}
