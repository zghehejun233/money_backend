package site.forum.web.teacher.service;

import org.springframework.stereotype.Service;
import site.forum.web.teacher.data.vo.CourseNotificationVo;
import site.forum.web.common.data.po.CourseInfo;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.data.po.Exam;
import site.forum.web.teacher.mapper.CourseMapper;
import site.forum.web.teacher.mapper.ExamMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private ExamMapper examMapper;

    public Result<?> getCourseNotification(Integer status, Long id) {
        List<CourseNotificationVo<CourseInfo>> vos = new ArrayList<>();
        courseMapper.selectCourseByStatusAndId(status, id)
                .forEach(courseInfo -> {
                    CourseNotificationVo<CourseInfo> vo = new CourseNotificationVo<>();
                    vo.setMessage("您有一门课程审核");
                    vo.setStatus(status);
                    vo.setPayload(courseInfo);
                    vos.add(vo);
                });
        return new Result<>().success(vos);
    }

    public Result<?> getExamNotification(Integer status, Long id) {
        List<CourseNotificationVo<Exam>> vos = new ArrayList<>();
        examMapper.selectExamByStatusAndId(status, id)
                .forEach(examInfo -> {
                    CourseNotificationVo<Exam> vo = new CourseNotificationVo<>();
                    vo.setMessage("您有一门考试审核");
                    vo.setStatus(status);
                    vo.setPayload(examInfo);
                    vos.add(vo);
                });
        return new Result<>().success(vos);
    }
}
