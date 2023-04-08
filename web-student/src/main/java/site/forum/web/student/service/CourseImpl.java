package site.forum.web.student.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.forum.web.common.data.po.CourseInfo;
import site.forum.web.common.data.po.CourseTime;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.data.vo.result.StudentInfoError;
import site.forum.web.student.data.po.Exam;
import site.forum.web.student.data.vo.ExamVo;
import site.forum.web.student.mapper.CourseInfoMapper;
import site.forum.web.student.mapper.CourseMapper;
import site.forum.web.student.mapper.ExamMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CourseImpl implements CourseService {
    @Resource
    private CourseMapper courseMapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Resource
    private ExamMapper examMapper;
    @Resource
    private RemoteCourseService remoteCourseService;

    @Override
    public Result<?> getAllCourses(Long id) {
        List<CourseInfo> courseInfoList = courseMapper.findALlByStudent(id);
        return (!courseInfoList.isEmpty()) ?
                new Result<>().success(courseInfoList) : new Result<>().fail(StudentInfoError.INFO_NOT_FOUND);
    }

    @Override
    public Result<?> getSchedule(Long id, Integer start, Integer end) {
        List<CourseInfo> courseInfoList = courseMapper.findALlByStudent(id);
        List<List<CourseTime>> courseTimeList = new ArrayList<>();

        List<HashMap<String, Object>> resultList = new ArrayList<>();

        for (CourseInfo courseInfo : courseInfoList) {
            if (courseInfo == null)
                continue;
            log.info("courseInfo: {}", courseInfo);
            Result<Map<String, List<Integer>>> courseTimeResult = remoteCourseService.getCourseTime(courseInfo.getId(), null ,start, end);
            log.info("courseTimeResult: {}", courseTimeResult.getData());

            HashMap<String, Object> info = new HashMap<>();
            info.put("name", courseInfo.getName());
            info.put("credit", courseInfo.getCredit());
            info.put("location", courseInfo.getLocation());
            info.put("time", courseTimeResult.getData());
            resultList.add(info);
        }

        return (resultList.isEmpty())
                ? new Result<String>(StudentInfoError.INFO_NOT_FOUND) : new Result<List<HashMap<String, Object>>>().success(resultList);
    }

    @Override
    public Result<?> getExams(Long studentId) {
        List<Exam> exams = examMapper.selectByStudent(studentId);
        List<ExamVo> examVos = new ArrayList<>();
        exams.forEach(exam -> examVos.add(new ExamVo(exam.getName(), exam.getDate(), exam.getBeginTime(), exam.getEndTime(), exam.getArea())));

        return (examVos.isEmpty()) ? new Result<>().fail() : new Result<>().success(examVos);
    }
}
