package site.forum.web.webcourse.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import site.forum.web.common.data.po.CourseInfo;
import site.forum.web.common.data.po.CourseTime;
import site.forum.web.common.data.vo.StudentWithScore;
import site.forum.web.common.data.vo.result.CourseInfoError;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.webcourse.mapper.CourseInfoMapper;
import site.forum.web.webcourse.util.ReflectionUtil;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseInfoService {
    @Resource
    private CourseInfoMapper courseInfoMapper;
    @Resource
    private ScoreService scoreService;

    public Result<?> addCourse(CourseInfo courseInfo) {
        return (courseInfoMapper.insert(courseInfo) == 1) ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> deleteCourse(Long id) {
        return (courseInfoMapper.deleteById(id) == 1) ? new Result<>().success() : new Result<>().fail(CourseInfoError.FAILED_TO_EDIT);
    }

    public Result<?> editCourseInfo(Long id, JSONObject data) {
        CourseInfo courseInfo = courseInfoMapper.selectById(id);
        if (courseInfo == null) {
            return new Result<>().fail(CourseInfoError.INFO_NOT_FOUND);
        }

        boolean flag = false;
        Map<String, Object> params = new HashMap<>();
        ReflectionUtil.getAllKey(data, params);

        try {
            for (Field field : CourseInfo.class.getDeclaredFields()) {
                if (params.containsKey(field.getName())) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();

                    if (type.isAssignableFrom(Float.class)) {
                        field.set(courseInfo, Float.valueOf((String) params.get(field.getName())));
                        flag = true;
                    } else if (type.isAssignableFrom(Integer.class)) {
                        field.set(courseInfo, Integer.valueOf((String) params.get(field.getName())));
                        flag = true;
                    } else {
                        try {
                            field.set(courseInfo, params.get(field.getName()));
                            flag = true;
                        } catch (IllegalArgumentException illegalArgumentException) {
                            illegalArgumentException.printStackTrace();
                            return new Result<>().fail(CourseInfoError.ACCESS_ILLEGAL);
                        }
                    }
                }
            }
        } catch (IllegalAccessException accessException) {
            accessException.printStackTrace();
            return new Result<>().fail(CourseInfoError.ACCESS_ILLEGAL);
        }

        courseInfoMapper.updateById(courseInfo);
        return flag ? new Result<>().success() : new Result<>().fail(CourseInfoError.FAILED_TO_EDIT);
    }

    public Result<?> getCoursesByCondition(String name, String teacher, Long teacherId, String courseId) {
        List<CourseInfo> courseInfos = courseInfoMapper.findAllByCondition(name, teacher, teacherId, courseId);
        return new Result<>().success(courseInfos);
    }


    public Result<?> getStudentByCourseId(Long courseId) {
        scoreService.updateScores(courseId);
        List<StudentWithScore> students = courseInfoMapper.selectAllStudentByCourse(courseId);
        return new Result<>().success(students);
    }

    public Result<?> getTimeByCourseAndWeek(Long courseId, Integer week, Integer start, Integer end) {
        List<CourseTime> courseTimes = courseInfoMapper.findAllTimes(courseId, week, start, end);
        if (courseTimes.isEmpty()) {
            return new Result<>().success(null);
        }
        List<Integer> weeks = new ArrayList<>(18);
        List<Integer> weekdays = new ArrayList<>(7);
        List<Integer> order = new ArrayList<>(5);

        for (CourseTime courseTime : courseTimes) {
            if (!weeks.contains(courseTime.getWeek())) {
                weeks.add(courseTime.getWeek());
            }
            if (!weekdays.contains(courseTime.getDay()) || !order.contains(courseTime.getOrder())) {
                weekdays.add(courseTime.getDay());
                order.add(courseTime.getOrder());
            }
        }

        Map<String, List<Integer>> data = new HashMap<>();
        data.put("weeks", weeks);
        data.put("weekday", weekdays);
        data.put("order", order);

        return new Result<>().success(data);
    }
}
