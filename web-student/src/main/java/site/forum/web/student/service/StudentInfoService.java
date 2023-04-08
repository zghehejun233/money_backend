package site.forum.web.student.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import site.forum.web.common.data.vo.result.Result;


public interface StudentInfoService {

    /**
     * 获取某个学生的所有信息
     *
     * @param userId 学号
     * @return 通用响应
     */
    Result<?> getStudentInfo(String userId);

    /**
     * 以分页的形式返回所有学生信息
     *
     * @param page  忘了
     * @param index 忘了
     * @return 通用响应
     */
    Result<?> getAllStudent(Integer page, Integer index);

    Result<?> editStudentInfo(String userId, JSONObject data);

    /**
     * 上传头像
     *
     * @return 通用响应
     */
    Result<?> uploadAvatar(String userId, MultipartFile avatar);

    Result<?> getPossibleStudent(Long id, String sameClass, String name, String userId, String major);

    /**
     * 获取学生的培养计划
     *
     * @param userId 学号
     * @return x
     */
    Result<?> getEducationPlan(String userId, String name, String type, Integer state, Integer high, Integer low);

}
