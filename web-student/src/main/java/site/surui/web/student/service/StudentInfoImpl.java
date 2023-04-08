package site.surui.web.student.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.surui.web.common.data.po.Student;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.data.vo.result.StudentInfoError;
import site.surui.web.common.util.FileUtil;
import site.surui.web.common.util.SQLUtil;
import site.surui.web.common.data.dto.ClassDto;
import site.surui.web.student.data.po.EducationPlan;
import site.surui.web.student.data.vo.StudentCommentVo;
import site.surui.web.student.mapper.EducationPlanMapper;
import site.surui.web.student.mapper.StudentMapper;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RefreshScope
@Slf4j
public class StudentInfoImpl implements StudentInfoService {
    @Value("${image.upload}")
    String picUploadUrl;
    @Value("${image.direct}")
    String picDirectUrl;
    @Value("${image.username}")
    String cloudUserName;
    @Value("${image.password}")
    String cloudPassword;

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private EducationPlanMapper educationPlanMapper;

    @Override
    public Result<?> getStudentInfo(String userId) {
        Student student = studentMapper.findByUserId(userId);
        if (student == null) {
            return new Result<String>(StudentInfoError.INFO_NOT_FOUND);
        }
        ClassDto classDto = studentMapper.selectClassNameByStudent(student.getId());
        student.setClassName(classDto.getName());
        student.setGrade(classDto.getGrade());

        return new Result<Student>().success(student);
    }

    @Override
    public Result<?> getAllStudent(Integer page, Integer index) {
        return null;
    }

    public Result<?> getPossibleStudent(Long id, String sameClass, String name, String userId, String major) {
        log.info("id: {}, sameClass: {}, name: {}, userId: {}, major: {}", id, sameClass, name, userId, major);

        name = buildRegex(name);
        log.info("search name: {}", name);
        major = buildRegex(major);

        List<Student> students = studentMapper.selectPossibleStudents(id, sameClass, name, userId, major);
        if (students.isEmpty()) {
            return new Result<>().fail();
        }
        List<StudentCommentVo> studentCommentVos = new ArrayList<>();
        students.forEach(student -> {
            StudentCommentVo studentCommentVo = new StudentCommentVo();
            studentCommentVo.setId(student.getId());
            studentCommentVo.setName(student.getName());
            studentCommentVo.setMajor(student.getMajor());
            studentCommentVo.setUserId(student.getUserId());
            student.setClassName(studentMapper.selectClassNameByStudent(student.getId()).getName());
            studentCommentVo.setClassName(student.getClassName());
            studentCommentVo.setAvatar(student.getAvatar());

            studentCommentVos.add(studentCommentVo);
        });

        return new Result<>().success(studentCommentVos);
    }

    @Override
    public Result<?> editStudentInfo(String userId, JSONObject data) {
        boolean flag = false;

        Map<String, Object> params = new HashMap<>();
        getAllKey(data, params);

        Student student = studentMapper.findByUserId(userId);
        if (student == null) {
            return new Result<String>(StudentInfoError.INFO_NOT_FOUND);
        }

        try {
            for (Field field : Student.class.getDeclaredFields()) {
                if (params.containsKey(field.getName())) {
                    field.setAccessible(true);
                    try {
                        if (field.getName().contains("birthday")) {
                            Date birthday = Date.valueOf(params.get(field.getName()).toString());
                            field.set(student, birthday);
                        } else if (field.getName().contains("area")) {
                            Long area = Long.valueOf(params.get(field.getName()).toString());
                            field.set(student, area);
                        } else {
                            field.set(student, params.get(field.getName()));
                        }
                    } catch (NullPointerException nullPointerException) {
                        nullPointerException.printStackTrace();
                    }
                    flag = true;
                }
            }
        } catch (IllegalAccessException accessException) {
            accessException.printStackTrace();
            return new Result<String>(StudentInfoError.FAIL_TO_EDIT_INFO);
        }

        studentMapper.updateById(student);

        return flag ? new Result<String>().success() : new Result<String>(StudentInfoError.FAIL_TO_EDIT_INFO);
    }


    @Override
    public Result<?> uploadAvatar(String userId, MultipartFile avatar) {
        if (avatar.isEmpty()) {
            return new Result<>().fail("请选择文件");
        }

        FileUtil fileUtil = new FileUtil(cloudUserName, cloudPassword);
        String directUrl = fileUtil.uploadAvatar(avatar, picUploadUrl, picDirectUrl, userId);
        if (directUrl == null) {
            return new Result<>().fail("上传失败");
        }

        Student student = studentMapper.findByUserId(userId);
        student.setAvatar(directUrl);
        studentMapper.updateById(student);

        return new Result<>().success();
    }


    @Override
    public Result<?> getEducationPlan(String userId, String name, String type, Integer state, Integer high, Integer low) {
        Student student = studentMapper.findByUserId(userId);
        if(name != null) {
            name = SQLUtil.buildRegex(name);
        }
        List<EducationPlan> educationPlans = educationPlanMapper.findAllByStudent(student.getId(), name, type, state, high, low);
        educationPlans.forEach(educationPlan -> {
            Integer _state = educationPlanMapper.getState(student.getId(), educationPlan.getId());
            educationPlan.setState(_state);
        });
        return (educationPlans.isEmpty()) ?
                new Result<String>(StudentInfoError.INFO_NOT_FOUND) : new Result<>().success(educationPlans);
    }

    private String buildRegex(String src) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        char[] tokens = src.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char token : tokens) {
            if (token != ' ') {
                sb.append(token).append("+");
                if (token != tokens[tokens.length - 1]) {
                    sb.append("|");
                }
            }
        }

        return sb.toString();
    }

    private Boolean isJsonObject(String jsonString) {
        try {
            JSONObject.parseObject(JSON.parseObject(jsonString, String.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void getAllKey(JSONObject jsonObject, Map<String, Object> param) {
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String string = jsonObject.getString(String.valueOf(entry.getKey()));
            param.put(entry.getKey(), string);
            if (isJsonObject(string)) {
                JSONObject son = JSONObject.parseObject(JSON.parseObject(string, String.class));
                getAllKey(son, param);
            }
        }
    }
}
