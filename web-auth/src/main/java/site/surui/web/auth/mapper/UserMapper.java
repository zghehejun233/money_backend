package site.surui.web.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import site.surui.web.common.data.po.User;

import java.util.Map;

/**
 * @author Guo Surui
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据userId查找对应的用户信息
     *
     * @param userId id
     * @return 封装好的用户对象
     */
    @Select("SELECT * FROM user_info WHERE user_id = #{id}")
    Map<String, Object> findUserByUserId(@Param("id") String userId);

    @Select("SELECT * FROM student WHERE user_id=#{id} LIMIT 1")
    Object isStudentExist(@Param("id") String userId);

    @Select("SELECT * FROM teacher WHERE user_id=#{id}")
    Object isTeacherExist(@Param("id") String userId);

    @Insert("INSERT INTO student (user_id, `name`) VALUES (#{id}, #{id})")
    void syncStudentInfo(@Param("id") String userId);

    @Select("SELECT id from student WHERE user_id=#{id}")
    Long getStudentIdByUserId(@Param("id") String userId);

    @Insert("INSERT INTO teacher(user_id, `name`) VALUES (#{id}, #{id})")
    void syncTeacherInfo(@Param("id") String userId);

    @Select("SELECT id from teacher WHERE user_id=#{id}")
    Long getTeacherIdByUserId(@Param("id") String userId);

    @Select("SELECT * FROM user_info WHERE user_id=#{userId}")
    User selectByUserId(@Param("userId") String userId);

    @Update("UPDATE user_info SET pwd=#{password} WHERE user_id=#{userId}")
    void updatePassword(@Param("userId") String userId, @Param("password") String password);
}
