package site.forum.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.teacher.data.po.Paper;

import java.util.List;

@Mapper
public interface PaperMapper extends BaseMapper<Paper> {

    @Select("SELECT p.* FROM paper p JOIN teacher t ON t.id=p.tid WHERE t.user_id= #{id}")
    List<Paper> getAllPapersByUserId(@Param("id") String userId);
}
