package site.surui.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import site.surui.web.common.data.po.BaseActivity;

import java.util.List;

@Mapper
public interface BaseActivityMapper extends BaseMapper<BaseActivity> {

    List<BaseActivity> selectActivity(@Param("type") Integer type, @Param("status") Integer status);

    @Select("SELECT s.name FROM student s JOIN activity a on s.id = a.maintainer WHERE a.id = #{id}")
    String getMaintainerName(@Param("id") Long activityId);

}
