package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.surui.web.student.data.po.Blog;
import site.surui.web.student.data.vo.BlogVo;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    @Select("SELECT b.id,b.title,b.content,s.name,b.time FROM blog b INNER JOIN student s on b.sid = s.id ORDER BY time DESC ")
    List<BlogVo> findAllBlogs();

    List<BlogVo> findByIdNameTitleContent(@Param("id") Long id,
                                          @Param("name") String name,
                                          @Param("title") String title,
                                          @Param("content") String content);


}
