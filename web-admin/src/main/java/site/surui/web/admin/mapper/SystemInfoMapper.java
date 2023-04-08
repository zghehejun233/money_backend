package site.surui.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemInfoMapper extends BaseMapper<Object> {

    @Select("SELECT COUNT(*) FROM student")
    Integer getStudentScale();

    @Select("SELECT COUNT(*) FROM admin")
    Integer getAdminScale();

    @Select("SELECT COUNT(*) FROM teacher")
    Integer getTeacherScale();
}
