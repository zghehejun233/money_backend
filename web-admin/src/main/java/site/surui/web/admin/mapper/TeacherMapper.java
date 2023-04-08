package site.surui.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import site.surui.web.common.data.po.Teacher;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    @Select("SELECT * FROM teacher WHERE id = #{id}")
    Teacher selectTeacherById(Long id);

    @Delete("DELETE FROM teacher WHERE id = #{id}")
    void deleteTeacherById(Long id);
}
