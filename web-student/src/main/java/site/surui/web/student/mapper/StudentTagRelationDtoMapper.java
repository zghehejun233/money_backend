package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.student.data.dto.StudentTagRelationDto;
import site.surui.web.student.data.vo.TagVo;

import java.util.List;

@Mapper
public interface StudentTagRelationDtoMapper extends BaseMapper<StudentTagRelationDto> {

    @Select("SELECT 1 FROM student_tag_relation WHERE sid=#{user} AND tag_id=#{id} LIMIT 1")
    Integer existTagByIdAndUserId(@Param("id") Long id, @Param("user") String userId);

    @Select("SELECT id FROM student_tag_relation WHERE sid=#{user} AND tag_id=#{id} LIMIT 1")
    Long findRelationByIdAndUserId(@Param("id") Long id, @Param("user") String userId);

    @Select("UPDATE student_tag_relation SET weight=weight+1 WHERE id=#{id}")
    void weightIncreaseById(@Param("id") Long id);

    @Select("SELECT t.id, t.tag, r.weight FROM student_tag_relation r JOIN tags t on r.tag_id = t.id WHERE r.sid=#{id}")
    List<TagVo> findAllTagsByStudent(@Param("id") String userId);
}
