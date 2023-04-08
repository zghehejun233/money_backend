package site.forum.web.teacher.service;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.forum.web.teacher.data.po.Paper;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.data.vo.result.TeacherInfoError;
import site.forum.web.common.util.FileUtil;
import site.forum.web.common.data.po.Teacher;
import site.forum.web.teacher.mapper.PaperMapper;
import site.forum.web.teacher.mapper.TeacherMapper;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TeacherService {
    @Value("${image.upload}")
    String picUploadUrl;
    @Value("${image.direct}")
    String picDirectUrl;
    @Value("${image.username}")
    String cloudUserName;
    @Value("${image.password}")
    String cloudPassword;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private PaperMapper paperMapper;

    public Result<?> getTeacherInfo(Long id, String email) {
        Teacher teacher = teacherMapper.findTeacherById(id);
        teacher.setPinyin(PinyinUtil.getPinyin(teacher.getName(), " "));
        teacher.setEmail(email);
        String courses = teacherMapper.getCourseNamesByTeacher(teacher.getId()).toString();
        teacher.setLectureCourse(courses.substring(1, courses.length() - 1));
        return new Result<>().success(teacher);
    }

    public Result<?> edit(Long id, JSONObject data) {
        boolean flag = false;

        Map<String, Object> params = new HashMap<>();
        getAllKey(data, params);

        Teacher teacher = teacherMapper.findTeacherById(id);

        try {
            for (Field field : Teacher.class.getDeclaredFields()) {
                if (params.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(teacher, params.get(field.getName()));
                    flag = true;
                }
            }
        } catch (IllegalAccessException accessException) {
            accessException.printStackTrace();
            return new Result<>().fail(TeacherInfoError.FAIL_TO_EDIT);
        }

        teacherMapper.updateById(teacher);

        return flag ? new Result<>().success() : new Result<>().fail(TeacherInfoError.FAIL_TO_EDIT);
    }

    public Result<?> uploadAvatar(Long id, String userId, MultipartFile avatar) {
        if (avatar.isEmpty()) {
            return new Result<>().fail("请选择文件");
        }

        FileUtil fileUtil = new FileUtil(cloudUserName, cloudPassword);
        String directUrl = fileUtil.uploadAvatar(avatar, picUploadUrl, picDirectUrl, userId);
        if (directUrl == null) {
            return new Result<>().fail("上传失败");
        }

        Teacher teacher = teacherMapper.findTeacherById(id);
        teacher.setAvatar(directUrl);
        teacherMapper.updateById(teacher);

        return new Result<>().success();
    }

    public Result<?> getPapers(String userId) {
        List<Paper> papers = paperMapper.getAllPapersByUserId(userId);
        return (papers.isEmpty()) ? new Result<>().fail(TeacherInfoError.INFO_NOT_FOUND) : new Result<>().success(papers);
    }


    public Result<?> addPaper(Long id, Paper paper) {
        Teacher teacher = teacherMapper.findTeacherById(id);
        paper.setTid(teacher.getId());

        return (paperMapper.insert(paper) == 1) ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> removePaper(Long id) {
        return (paperMapper.deleteById(id) == 1) ? new Result<>().success() : new Result<>().fail();
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
