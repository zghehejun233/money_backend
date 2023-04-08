package site.surui.web.teacher.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.po.CourseTime;
import site.surui.web.common.data.po.Student;
import site.surui.web.common.data.vo.StudentWithScore;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.data.vo.result.StudentInfoError;
import site.surui.web.common.data.vo.result.TeacherInfoError;
import site.surui.web.teacher.data.dto.CourseTimeDto;
import site.surui.web.common.data.po.Exam;
import site.surui.web.common.data.po.Score;
import site.surui.web.common.data.po.Teacher;
import site.surui.web.teacher.data.vo.ExamStatusVo;
import site.surui.web.teacher.data.vo.ExamVo;
import site.surui.web.teacher.mapper.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TeachingService {
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private ExamMapper examMapper;
    @Resource
    private CourseTimeMapper courseTimeMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseService courseService;


    public Result<?> findCourses(Long id) {
        Teacher teacher = teacherMapper.findTeacherById(id);
        Result<List<CourseInfo>> callResult = courseService.getCourse(null, null, teacher.getId(), null);
        List<CourseInfo> courseInfos = (List<CourseInfo>) callResult.getData();
        return  new Result<List<CourseInfo>>().success(courseInfos);
    }

    public Result<?> addCourse(Long id, CourseInfo courseInfo) {
        CourseInfo course = courseMapper.findCourseByCourseId(courseInfo.getCourseId());
        if(course!=null) return new Result<>().fail("添加失败，课程号已存在！");
        Teacher teacher = teacherMapper.findTeacherById(id);
        courseInfo.setTeacher(teacher.getName());
        courseInfo.setTid(teacher.getId());
        Result<?> callResult = courseService.addCourse(courseInfo);
        return (callResult.getCode() == 0) ? new Result<>().success(courseInfo.getId()) : new Result<>().fail(callResult.getMessage());
    }

    public Result<?> addCourseTime(Long courseId, CourseTimeDto courseTimeDto) {
        AtomicInteger error = new AtomicInteger(0);
        AtomicInteger success = new AtomicInteger(0);
        if (courseTimeDto.getWeekday().size() != courseTimeDto.getOrder().size()) {
            return new Result<>().fail("weekday and order size not equal");
        }
        courseTimeDto.getWeeks().forEach(week -> {
            for (int i = 0; i < courseTimeDto.getWeekday().size(); i++) {
                Integer weekday = courseTimeDto.getWeekday().get(i);
                Integer order = courseTimeDto.getOrder().get(i);
                CourseTime courseTime = courseTimeMapper.findCourseTimeByDetail(week, weekday, order);
                if (courseTime == null) {
                    courseTimeMapper.addCourseTime(week, weekday, order);
                    courseTime = courseTimeMapper.findCourseTimeByDetail(week, weekday, order);
                }
                if (courseTimeMapper.findRelation(courseId, courseTime.getId()) != null) {
                    error.getAndIncrement();
                } else {
                    try {
                        courseTimeMapper.addCourseRelation(courseId, courseTime.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        error.getAndIncrement();
                    }
                    success.getAndIncrement();
                }
            }
        });
        return new Result<>().success("success: " + success + ", error: " + error);
    }

    public Result<?> deleteCourseTime(Long courseId, CourseTimeDto time) {
        AtomicInteger error = new AtomicInteger();
        AtomicInteger success = new AtomicInteger();
        time.getWeeks().forEach(week -> {
            for (int i = 0; i < time.getWeekday().size(); i++) {
                Integer weekday = time.getWeekday().get(i);
                Integer order = time.getOrder().get(i);
                CourseTime courseTime = courseTimeMapper.findCourseTimeByDetail(week, weekday, order);
                if (courseTime != null) {
                    try {
                        courseTimeMapper.deleteRelatedCourseByTimeId(courseId, courseTime.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        error.getAndIncrement();
                    }
                    success.getAndIncrement();
                }
            }
        });
        courseTimeMapper.deleteRelatedCourseByCourseId(courseId);
        return new Result<>().success("success: " + success + ", error: " + error);
    }

    public Result<?> enrichCapacity(Long id, Integer capacity) {
        Map<String, Integer> data = new HashMap<>();
        data.put("capacity", capacity);
        String json = JSON.toJSONString(data);
        Result<?> editResult = courseService.editCourseInfo(id, JSONObject.parseObject(json));
        return (editResult.getCode() == 0) ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> getStudentList(Long courseId) {
        Result<List<StudentWithScore>> callResult = courseService.getStudentWithScoreList(courseId);
        return new Result<>().success(callResult);
    }

    public Result<?> setScores(Long courseId, List<Score> scores) {
        if (scores.isEmpty())
            return new Result<>().fail("没有添加成绩！");
        for (Score score : scores) {
            score.setCid(courseId);
            Score score1 = scoreMapper.selectByCidSid(courseId, score.getSid());
            if (score1 != null) {
                score.setId(score1.getId());
                scoreMapper.updateById(score);
            } else {
                scoreMapper.insert(score);
            }
        }
        return new Result<>().success();
    }

    @Transactional
    public Result<?> addExam(Long courseId, Long teacherId, ExamVo examVo) {
        if (examVo == null) {
            return new Result<>().fail("课程信息不能为空");
        }
        Exam exam = new Exam(null, examVo.getName(), examVo.getDate(), examVo.getBetime(), examVo.getEndtime(), examVo.getArea(), teacherId, courseId, 0);
        if (examMapper.insert(exam) != 1) {
            return new Result<>().fail("考试添加失败");
        }
        return new Result<>().success(exam.getId());
    }

    public Result<?> getExams(Long teacherId) {
        List<Exam> exams = examMapper.selectByTeacher(teacherId);
        List<ExamVo> examVos = new ArrayList<>();
        exams.forEach(exam -> {
            ExamVo examVo = new ExamVo(exam.getName(), exam.getDate(), exam.getBeginTime(), exam.getEndTime(), exam.getArea());
            examVos.add(examVo);
        });
        return (examVos.isEmpty()) ?
                new Result<>(TeacherInfoError.INFO_NOT_FOUND) : new Result<>().success(examVos);
    }

    public Result<?> getExamStudents(Long courseId) {
        List<Student> students = examMapper.selectStudentsByCourse(courseId);
        return (students.isEmpty()) ? new Result<>(TeacherInfoError.INFO_NOT_FOUND) : new Result<>().success(students);
    }

    public Result<?> addStudentExamRelation(Long teacherId, Long courseId, List<Long> studentIds) {
        Exam exam = examMapper.selectExamByCourseAndTeacher(courseId, teacherId);
        if (exam == null) {
            return new Result<>().fail("未查询到对应课程");
        }
        studentIds.forEach(id -> examMapper.insertExamRelation(exam.getId(), id));

        return new Result<>().success();
    }

    public Result<?> getExamStatusById(Long teacherId) {
        List<ExamStatusVo> examStatus = examMapper.selectExamStatusByTeacherId(teacherId);
        return new Result<>().success(examStatus);
    }

    public Result<?> getSchedule(Long id, Integer start, Integer end) {
        List<CourseInfo> courseInfoList = courseMapper.findALlByTeacherId(id);

        List<List<CourseTime>> courseTimeList = new ArrayList<>();
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        for (CourseInfo courseInfo : courseInfoList) {
            if (courseInfo == null)
                continue;
            Result<Map<String, List<Integer>>> courseTimeResult = courseService.getCourseTime(courseInfo.getId(), null, start, end);
            HashMap<String, Object> info = new HashMap<>();
            info.put("id", courseInfo.getId());
            info.put("name", courseInfo.getName());
            info.put("credit", courseInfo.getCredit());
            info.put("location", courseInfo.getLocation());
            info.put("time", courseTimeResult.getData());
            resultList.add(info);
        }

        return (resultList.isEmpty())
                ? new Result<String>(StudentInfoError.INFO_NOT_FOUND) : new Result<List<HashMap<String, Object>>>().success(resultList);
    }

}
