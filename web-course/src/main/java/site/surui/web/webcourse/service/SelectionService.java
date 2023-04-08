package site.surui.web.webcourse.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.po.CourseTime;
import site.surui.web.common.data.po.Period;
import site.surui.web.common.data.vo.StudentWithScore;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.util.SQLUtil;
import site.surui.web.webcourse.data.dto.SelectionDto;
import site.surui.web.webcourse.data.dto.SelectionEventDto;
import site.surui.web.webcourse.data.event.SelectionEvent;
import site.surui.web.webcourse.data.vo.CourseSelectionVo;
import site.surui.web.webcourse.mapper.CourseInfoMapper;
import site.surui.web.webcourse.mapper.SelectionMapper;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class SelectionService {
    @Resource
    private CourseInfoMapper courseInfoMapper;
    @Resource
    private SelectionMapper selectionMapper;
    @Resource
    private ApplicationEventPublisher publisher;

    public Result<?> setSelectionTime(Long id, Date start, Date end) {
        CourseInfo courseInfo = courseInfoMapper.selectById(id);
        if (courseInfo == null) {
            return new Result<>().fail();
        }
        courseInfo.setStart(start);
        courseInfo.setEnd(end);
        return (courseInfoMapper.updateById(courseInfo) == 1) ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> findAllStudentByCourse(Long courseId) {
        List<StudentWithScore> students = courseInfoMapper.selectAllStudentByCourse(courseId);
        return (students.isEmpty()) ? new Result<>().fail() : new Result<>().success();
    }

    public Result<?> selectCourse(Long courseId, Long studentId) {
        Date now = new Date();
        now.setTime(now.getTime() + 8 * 60 * 60 * 1000);
        Period period = selectionMapper.getLastPeriod();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (period != null) {
            try {
                Date b = dateFormat.parse(period.getBegin()), e = dateFormat.parse(period.getEnd());
                if (now.after(b) && e.after(now)) {
                    publisher.publishEvent(new SelectionEvent(this, new SelectionEventDto(studentId, courseId)));
                    return new Result<>().success("选课成功！");
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return new Result<>().success("选课失败，当前不处于任何选课轮次中！");
    }

    public Result<?> removeSelection(Long courseId, Long studentId) {
        Date now = new Date();
        now.setTime(now.getTime() + 8 * 60 * 60 * 1000);
        Period period = selectionMapper.getLastPeriod();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (period != null) {
            try {
                Date b = dateFormat.parse(period.getBegin()), e = dateFormat.parse(period.getEnd());
                if (now.after(b) && e.after(now)) {
                    List<SelectionDto> selectionDtos = selectionMapper.findByCourseIdAndStudentId(courseId, studentId);
                    selectionDtos.forEach(selectionDto -> selectionMapper.deleteById(selectionDto.getId()));
                    return new Result<>().success("退选成功！");
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return new Result<>().success("退选失败，当前不处于任何选课轮次中！");

    }

    public Result<?> getPossibleCourse(Long studentId, String name, String type, Integer state, Integer low, Integer high) {
        name = SQLUtil.buildRegex(name);
        Date now = new Date();
        now.setTime(now.getTime() + 8 * 60 * 60 * 1000);
        Period period = selectionMapper.getLastPeriod();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<CourseInfo> courseInfoList =
                courseInfoMapper.findCourseByNameAndCredit(name, type, state, low, high);

        List<CourseSelectionVo> courseSelectionVoList = new ArrayList<>();
        courseInfoList.forEach(
                courseInfo -> {
                    boolean inPeriod;
                    if (period != null) {
                        try {
                            Date b = dateFormat.parse(period.getBegin()), e = dateFormat.parse(period.getEnd());
                            inPeriod = now.after(b) && now.before(e);
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        inPeriod = true;
                    }

                    if (courseInfo.getStatus() == 1 && inPeriod) {
                        CourseSelectionVo courseSelectionVo = buildVo(studentId, courseInfo);
                        if (courseSelectionVo != null) {
                            courseSelectionVoList.add(courseSelectionVo);
                        } else {
                            log.warn("buildVo failed, courseInfo: {}", courseInfo);
                        }
                    }

                }
        );

        return new Result<>().success(courseSelectionVoList);

    }

    private CourseSelectionVo buildVo(Long studentId, CourseInfo courseInfo) {
        List<CourseTime> courseTimes = courseInfoMapper.findAllTimes(courseInfo.getId(), null, null, null);

        int selected = courseInfoMapper.countByCourseId(courseInfo.getId());
        String teacherName = courseInfoMapper.getTeacherName(courseInfo.getTid());
        String type = courseInfoMapper.getType(courseInfo.getId());

        List<Integer> weeks = new ArrayList<>(18);
        List<Integer> weekdays = new ArrayList<>(7);
        List<Integer> order = new ArrayList<>(5);

        for (CourseTime courseTime : courseTimes) {
            if (!weeks.contains(courseTime.getWeek())) {
                weeks.add(courseTime.getWeek());
            }
            if (!weekdays.contains(courseTime.getDay()) && !order.contains(courseTime.getOrder())) {
                weekdays.add(courseTime.getDay());
                order.add(courseTime.getOrder());


            }
        }

        Map<String, List<Integer>> data = new HashMap<>();
        data.put("weeks", weeks);
        data.put("weekday", weekdays);
        data.put("order", order);

        CourseSelectionVo courseSelectionVo = new CourseSelectionVo();
        try {
            courseSelectionVo.setId(courseInfo.getId());
            courseSelectionVo.setCourseId(courseInfo.getCourseId());
            courseSelectionVo.setName(courseInfo.getName());
            courseSelectionVo.setTeacher(teacherName);
            courseSelectionVo.setCredit(courseInfo.getCredit());
            courseSelectionVo.setType(type);
            courseSelectionVo.setLocation(courseInfo.getLocation());
            courseSelectionVo.setCapacity(courseInfo.getCapacity());
            courseSelectionVo.setSelected(selected);
            courseSelectionVo.setIsSelected(selectionMapper.isSelected(courseInfo.getId(), studentId) != null);
            courseSelectionVo.setTime(data);
        } catch (Exception e) {
            log.error("buildVo failed, courseInfo: {}", courseInfo);
            return courseSelectionVo;
        }

        return courseSelectionVo;
    }
}
