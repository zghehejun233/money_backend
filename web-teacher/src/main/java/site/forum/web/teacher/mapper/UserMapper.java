package site.forum.web.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.forum.web.common.data.po.User;

import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据userId查找对应的用户信息
     *
     * @param userId id
     * @return 封装好的用户对象
     */
    @Select("SELECT * FROM user_info WHERE user_info.user_id=#{id}")
    Map<String, Object> findUserByUserId(@Param("id") String userId);

    @Select("SELECT * FROM user_info WHERE user_info.user_id=#{id}")
    User getUser(@Param("id") String userId);

}
