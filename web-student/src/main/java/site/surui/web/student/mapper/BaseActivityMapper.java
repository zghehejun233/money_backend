package site.surui.web.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import site.surui.web.common.data.po.BaseActivity;
import site.surui.web.common.data.po.Student;

import java.util.List;

@Mapper
public interface BaseActivityMapper extends BaseMapper<BaseActivity> {

    @Select("SELECT a.* FROM activity a JOIN student_activity_relation sar ON a.id=sar.aid WHERE sar.sid=#{sid}")
    List<BaseActivity> findAllByStudent(@Param("sid") Long id);

    // @Select("SELECT a.* FROM activity a JOIN student s on a.sid = s.id WHERE s.user_id=#{sid} AND a.name=#{name}")
    // BaseActivity findAll(@Param("sid")String sid, @Param("name")String name);

    @Select("SELECT  * FROM activity WHERE id=#{id}")
    BaseActivity findActivityById(@Param("id") String id);

    // @Update("UPDATE activity SET sid=#{sid} WHERE id=#{id}")
    // void setSid(@Param("id")String id, @Param("sid")String sid);

    @Delete("DELETE FROM activity WHERE id=#{id}")
    int deleteByIdInt(@Param("id")String id);

    @Select("SELECT  * FROM activity WHERE id=#{id}")
    BaseActivity findById(@Param("id") String id);

    @Insert("INSERT INTO student_activity_relation (sid, aid) VALUES (#{sid}, #{aid})")
    Integer insertRelation(@Param("sid") Long sid, @Param("aid") Long aid);

    @Select("SELECT maintainer FROM activity WHERE id=#{aid}")
    Long findMaintainerByAid(@Param("aid") Long aid);
    @Delete("DELETE FROM student_activity_relation WHERE aid=#{aid}")
    Integer deleteRelation(@Param("aid") Long aid);

    @Delete("DELETE FROM student_activity_relation WHERE sid=#{sid} AND aid=#{aid}")
    Integer deleteRelationBySid(@Param("sid") Long sid, @Param("aid") Long aid);

    @Select("SELECT s.name FROM student s JOIN student_activity_relation sar on s.id = sar.sid WHERE sar.aid=#{aid}")
    List<String> selectAllParticipants(@Param("aid") Long aid);

    @Select("SELECT s.* FROM student s JOIN activity a on s.id = a.maintainer WHERE a.id=#{aid} AND a.status=1")
    List<Student> selectAllByActivityId(@Param("aid") Long aid);
}
