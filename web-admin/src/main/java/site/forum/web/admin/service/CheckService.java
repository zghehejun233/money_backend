package site.forum.web.admin.service;

import org.springframework.stereotype.Service;
import site.forum.web.admin.data.vo.BaseActivityVo;
import site.forum.web.admin.data.vo.ExamVo;
import site.forum.web.admin.mapper.CheckMapper;
import site.forum.web.common.data.po.CourseInfo;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CheckService {

    @Resource
    private CheckMapper checkMapper;

    public Result<?> getCheckingCourse() {
        List<CourseInfo> courseInfos = checkMapper.selectCheckingCourse();
        return new Result<>().success(courseInfos);
    }

    public Result<?> passCourse(Long id) {
        checkMapper.setCoursePass(id);
        return new Result<>().success();
    }

    public Result<?> refuseCourse(Long id) {
        checkMapper.setCourseRefused(id);
        return new Result<>().success();
    }

    public Result<?> getCheckingExam() {
        List<ExamVo> exams = checkMapper.selectCheckingExam();
        return new Result<>().success(exams);
    }

    public Result<?> passExam(Long id) {
        checkMapper.setExamPass(id);
        return new Result<>().success();
    }

    public Result<?> refuseExam(Long id) {
        checkMapper.setExamRefused(id);
        return new Result<>().success();
    }

    public Result<?> getCheckingActivity() {
        List<BaseActivityVo> activityVos = checkMapper.selectCheckingActivity();
        for (BaseActivityVo activityVo : activityVos){
            activityVo.setTypeName(getActivityType(activityVo.getType()));
        }
        return new Result<>().success(activityVos);
    }

    public Result<?> passActivity(Long activityId, Float score) {
        if (checkMapper.setActivityPass(activityId, score) == 1) {
            return new Result<>().success();
        }
        return new Result<>().fail();
    }

    public Result<?> refuseActivity(Long activityId) {
        if (checkMapper.setActivityRefused(activityId) == 1) {
            return new Result<>().success();
        }
        return new Result<>().fail();
    }

    private String getActivityType(Integer type) {
        switch (type) {
            case 0:
                return "社会实践";
            case 1:
                return "学生工作";
            case 2:
                return "志愿服务";
            case 3:
                return "学科竞赛";
            case 4:
                return "科研成果";
            default:
                return "未知类型";
        }
    }
}
