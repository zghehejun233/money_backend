package site.forum.web.student.service;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.forum.web.common.data.dto.Notification;
import site.forum.web.common.data.event.PublishNotificationEvent;
import site.forum.web.common.data.po.BaseActivity;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.util.ActivityInfoUtil;
import site.forum.web.common.util.SQLUtil;
import site.forum.web.student.data.vo.SocialPracticeVo;
import site.forum.web.student.data.vo.VoluntaryVo;
import site.forum.web.student.mapper.BaseActivityMapper;
import site.forum.web.student.mapper.StudentMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class ActivityService {
    @Resource
    private BaseActivityMapper baseActivityMapper;
    @Resource
    private ApplicationEventPublisher publisher;
    @Resource
    private StudentMapper studentMapper;

    private void addParticipants(String participantIds, Long id) {
        String[] ids = participantIds.split(",");
        Stream.of(ids)
                .filter(s -> !s.equals(""))
                .forEach(participantId -> {
                    try {
                        Long pid = Long.parseLong(participantId);
                        baseActivityMapper.insertRelation(pid, id);
                    } catch (ClassCastException e) {
                        log.error("参与者id转换错误");
                    }
                });
    }

    public Result<?> searchStudents(String name, String userId) {
        SQLUtil.buildRegex(name);
        List<Student> students = studentMapper.selectStudentsForSocialActivity(name, userId);
        List<SearchResult> vos = new ArrayList<>();

        students.forEach(student -> {
            SearchResult vo = new SearchResult(student.getId(), student.getUserId(), student.getName(), student.getAvatar());
            vos.add(vo);
        });

        return new Result<>().success(vos);
    }


    public <T> Result<?> addActivity(Long id, String type, T activityVo, String participantIds) {
        if (activityVo == null) {
            return new Result<>().fail("空内容");
        }

        boolean hasParticipant = false;

        if (activityVo instanceof SocialPracticeVo) {
            SocialPracticeVo vo = (SocialPracticeVo) activityVo;
            vo.setParticipants(null);
            hasParticipant = true;
        } else if (activityVo instanceof VoluntaryVo) {
            VoluntaryVo vo = (VoluntaryVo) activityVo;
            vo.setParticipants(null);
            hasParticipant = true;
        }

        BaseActivity activity = ActivityInfoUtil.activity2Base(activityVo);
        ActivityInfoUtil.initDefaultValue(activity, id, type);

        try {
            baseActivityMapper.insert(activity);
        } catch (Exception e) {
            log.warn("插入失败", e);
            return new Result<>().fail("添加失败");
        }
        if (!hasParticipant) {
            baseActivityMapper.insertRelation(id, activity.getId());
        } else {
            addParticipants(participantIds, activity.getId());
        }

        Notification notification = Notification.buildNotificationForAdmin(null, id, -1, "summary", JSONObject.toJSONString(activity));
        log.warn("notification: {}", notification);
        publisher.publishEvent(new PublishNotificationEvent(this, notification));
        return new Result<>().success("添加成功");
    }

    public Result<?> editActivity(Long studentId, Long id, JSONObject data) {
        BaseActivity activity = baseActivityMapper.selectById(id);
        ActivityInfoUtil.editActivity(data, activity);
        baseActivityMapper.updateById(activity);

        return new Result<>().success();
    }


    public Result<?> deleteActivity(Long studentId, Long id) {
        BaseActivity activity = baseActivityMapper.selectById(id);
        try {
            return deleteBaseActivity(activity, studentId);
        } catch (Exception e) {
            log.warn("删除失败", e);
            return new Result<>().fail("删除失败");
        }
    }

    public <T> Result<?> getActivity(Long id, String type, Class<T> clazz) {
        List<BaseActivity> activities = baseActivityMapper.findAllByStudent(id);

        List<T> vos = new ArrayList<>();
        activities.stream()
                .filter(activity -> activity.getType().equals(ActivityInfoUtil.type2Code(type)))
                .forEach(activity -> {
                    T vo = ActivityInfoUtil.base2Activity(activity, clazz);
                    if (clazz.equals(SocialPracticeVo.class)) {
                        SocialPracticeVo temp = (SocialPracticeVo) vo;
                        assert temp != null;
                        temp.setParticipants(baseActivityMapper.selectAllParticipants(activity.getId()));
                    } else if (clazz.equals(VoluntaryVo.class)) {
                        VoluntaryVo temp = (VoluntaryVo) vo;
                        assert temp != null;
                        temp.setParticipants(baseActivityMapper.selectAllParticipants(activity.getId()));
                    }
                    vos.add(vo);
                });

        return new Result<>().success(vos);
    }

    public Result<?> addParticipants(Long studentId, Long id, List<Long> participants) {
        if (!studentId.equals(baseActivityMapper.findMaintainerByAid(id))) {
            return new Result<>().fail("无权限");
        }

        participants.forEach(participant -> baseActivityMapper.insertRelation(participant, id));
        return new Result<>().success("添加成功");
    }

    public Result<?> deleteParticipants(Long studentId, Long id, List<Long> participants) {
        BaseActivity activity = baseActivityMapper.selectById(id);
        if (activity == null) {
            return new Result<>().fail("无此活动");
        }
        if (!activity.getMaintainer().equals(studentId)) {
            return new Result<>().fail("不是活动的Maintainer，无权限");
        }

        participants.forEach(participant -> baseActivityMapper.deleteRelationBySid(participant, id));
        return new Result<>().success("删除成功");
    }

    private void insertBaseActivity(BaseActivity activity, Long id) {
        baseActivityMapper.insert(activity);
        baseActivityMapper.insertRelation(id, activity.getId());
    }

    private Result<?> deleteBaseActivity(BaseActivity activity, Long id) {
        if (activity == null) {
            return new Result<>().fail("无此活动");
        }
        if (!activity.getMaintainer().equals(id)) {
            return new Result<>().fail("不是活动的Maintainer，无权限");
        }
        baseActivityMapper.deleteById(id);
        baseActivityMapper.deleteRelation(activity.getId());
        return new Result<>().success();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static
    class SearchResult {
        private Long id;
        private String userId;
        private String name;
        private String avatar;
    }
}
