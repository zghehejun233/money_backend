package site.surui.web.webcourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.surui.web.common.data.po.Student;
import site.surui.web.webcourse.data.po.Score;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    List<Score> selectAll();

}
