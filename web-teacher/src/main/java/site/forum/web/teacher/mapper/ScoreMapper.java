package site.forum.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.common.data.po.Score;

@Mapper
public interface ScoreMapper extends BaseMapper<Score> {

    @Select("SELECT * FROM score WHERE cid=#{cid} AND sid=#{sid}")
    Score selectByCidSid(@Param("cid") Long cid, @Param("sid") Long sid);
}
