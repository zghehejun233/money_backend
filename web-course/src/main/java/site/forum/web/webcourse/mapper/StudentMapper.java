package site.forum.web.webcourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.forum.web.common.data.po.Student;
import site.forum.web.webcourse.data.po.Score;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    List<Score> selectAll();

}
