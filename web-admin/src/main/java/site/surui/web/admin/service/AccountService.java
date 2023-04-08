package site.surui.web.admin.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.surui.web.admin.mapper.StudentMapper;
import site.surui.web.admin.mapper.TeacherMapper;
import site.surui.web.admin.mapper.UserMapper;
import site.surui.web.common.data.dto.ClassDto;
import site.surui.web.common.data.po.Student;
import site.surui.web.common.data.po.Teacher;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.util.AESUtil;
import site.surui.web.common.util.ReflectionUtil;

import javax.annotation.Resource;
import javax.el.MethodNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class AccountService {
    @Resource
    private UserMapper mapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private UserRemoteService userRemoteService;
    @Resource
    private StudentMapper studentMapper;

    private final AESUtil aesUtil = new AESUtil();

    public Result<?> getAllUserInfo(int role) {
        switch (role) {
            case 1:
                return new Result<>().success(mapper.selectAllTeacher());
            case 2:
                List<Student> students = mapper.selectAllStudent();
                for(Student student : students){
                    ClassDto classDto = studentMapper.selectClassNameByStudent(student.getId());
                    student.setClassName(classDto.getName());
                    student.setGrade(classDto.getGrade());
                }
                return new Result<>().success(students);
            default:
                return new Result<>().fail("不合法的参数");
        }
    }

    @Transactional
    public Result<?> addNewTeacher(Teacher teacher, String password) {
        User user = new User();
        user.setRole(1);
        user.setUserId(teacher.getUserId());
        user.setPassword(password);
        user.setEmail(teacher.getEmail());
        try {
            Result<?> result = userRemoteService.addUser(new ArrayList<>() {{
                add(user);
            }});
            if (result.getCode() != 0) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>().fail("添加失败");
        }


        teacher.setId(null);
        teacher.setPinyin(null);
        teacher.setEmail(null);
        try {
            teacherMapper.insert(teacher);
        } catch (Exception e) {
            log.error("添加教师失败");
            e.printStackTrace();
            return new Result<>().fail("添加教师失败");
        }
        return new Result<>().success();
    }

    public Result<?> deleteTeacher(Long userId) {
        try {
            Teacher teacher = teacherMapper.selectTeacherById(userId);
            mapper.deleteUser(teacher.getUserId());
            teacherMapper.deleteTeacherById(userId);
        } catch (Exception e) {
            log.error("删除教师失败");
            e.printStackTrace();
            return new Result<>().fail("删除教师失败");
        }
        return new Result<>().success();
    }

    public Result<?> editTeacherInfo(JSONObject data, Long userId) {
        data.remove("id");
        Teacher teacher = teacherMapper.selectTeacherById(userId);

        Map<String, Object> params = new HashMap<>();
        ReflectionUtil.getAllKey(data, params);

        Stream.of(teacher.getClass().getDeclaredFields())
                .filter(field -> params.containsKey(field.getName()))
                .forEach(field -> {
                            try {
                                setFieldByType(teacher, params, field);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );
        try {
            teacherMapper.updateById(teacher);
        } catch (Exception e) {
            log.error("更新教师信息失败");
            e.printStackTrace();
            return new Result<>().fail("更新教师信息失败");
        }
        return new Result<>().success();
    }


    public Result<?> deleteUser(Long userId) {
        User user = mapper.selectById(userId);
        if (user == null) {
            return new Result<>().fail("用户不存在");
        }
        switch (user.getRole()) {
            case 3:
                return new Result<>().fail("无法删除管理员");
            case 2:
                Student student = mapper.selectStudentById(user.getUserId());
                if (student != null) {
                    mapper.deleteStudentById(student.getId());
                }
                mapper.deleteById(userId);
            case 1:
                Teacher teacher = mapper.selectTeacherById(user.getUserId());
                if (teacher != null) {
                    mapper.deleteTeacherById(teacher.getId());
                }
                mapper.deleteById(userId);
            default:
                mapper.deleteById(userId);
        }
        return new Result<>().success();
    }


    private static <T> void setFieldByType(T activity, Map<String, Object> param, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        switch (field.getType().getName()) {
            case "java.lang.String":
                field.set(activity, String.valueOf(param.get(field.getName())));
                break;
            case "java.lang.Integer":
                field.set(activity, Integer.valueOf(String.valueOf(param.get(field.getName()))));
                break;
            case "java.lang.Float":
                field.set(activity, Float.valueOf(String.valueOf(param.get(field.getName()))));
                break;
            case "java.lang.Long":
                field.set(activity, Long.valueOf(String.valueOf(param.get(field.getName()))));
                break;
            default:
                throw new MethodNotFoundException("未找到对应的类型");
        }
    }

    @Transactional
    public Result<?> addNewStudent(Student student, String password) {
        User user = new User();
        user.setRole(2);
        user.setUserId(student.getUserId());
        user.setPassword(aesUtil.encryptDataWithCasId(student.getUserId(), password));
        user.setEmail("");
        mapper.insert(user);
        student.setId(null);
        try {
            studentMapper.insert(student);
        } catch (Exception e) {
            log.error("添加学生失败");
            e.printStackTrace();
            return new Result<>().fail("添加学生失败");
        }

        return new Result<>().success("添加学生成功");
    }

    public Result<?> deleteStudent(Long id) {
        try {
            Student student = studentMapper.selectById(id);
            mapper.deleteUser(student.getUserId());
            studentMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除学生失败");
            e.printStackTrace();
            return new Result<>().fail("删除学生失败");
        }
        return new Result<>().success("删除学生成功");
    }

    public Result<?> editStudentInfo(JSONObject data, Long id) {
        data.remove("id");
        Student student = studentMapper.selectById(id);
        if (student == null) return new Result<>().fail("学生不存在");
        Map<String, Object> params = new HashMap<>();
        ReflectionUtil.getAllKey(data, params);
        Stream.of(student.getClass().getDeclaredFields())
                .filter(field -> params.containsKey(field.getName()))
                .forEach(field -> {
                            try {
                                setFieldByType(student, params, field);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );
        studentMapper.updateById(student);
        return new Result<>().success();
    }

    public Result<?> getAllClass() {
        List<ClassDto> classes = studentMapper.selectAllClass();
        return new Result<>().success(classes);
    }

    public Result<?> addAClass(String grade, String className) {
        studentMapper.insertNewClass(className,grade);
        return new Result<>().success();
    }
}
