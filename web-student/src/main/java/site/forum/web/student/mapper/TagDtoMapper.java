package site.forum.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.student.data.dto.TagDto;

@Mapper
public interface TagDtoMapper extends BaseMapper<TagDto> {

    @Select("SELECT * FROM tags WHERE tag=#{tag} LIMIT 1")
    Integer existTagByName(@Param("tag") String tag);

    @Select("SELECT * FROM tags WHERE tag=#{tag}")
    TagDto findByName(@Param("tag") String tag);
}
