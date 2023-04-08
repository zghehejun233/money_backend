package site.forum.web.admin.service;

import org.springframework.stereotype.Service;
import site.forum.web.admin.data.vo.ActivityVo;
import site.forum.web.admin.mapper.BaseActivityMapper;
import site.forum.web.common.data.po.BaseActivity;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.common.data.vo.result.StudentInfoError;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {
    @Resource
    private BaseActivityMapper baseActivityMapper;

    public Result<?> getCheckingActivity(Integer type, Integer status) {
        List<BaseActivity> baseActivities = baseActivityMapper.selectActivity(type, status);
        if (baseActivities.isEmpty()) {
            return new Result<>(StudentInfoError.INFO_NOT_FOUND);
        }
        List<ActivityVo> activityVos = new ArrayList<>();
        baseActivities.forEach(baseActivity -> {
            ActivityVo activityVo = new ActivityVo();
            activityVo.setId(baseActivity.getId());
            activityVo.setName(baseActivity.getName());
            activityVo.setMaintainer(baseActivityMapper.getMaintainerName(baseActivity.getId()));
            activityVo.setType(getActivityType(baseActivity.getType()));
            activityVo.setStatus(getActivityStatus(baseActivity.getStatus()));
            activityVos.add(activityVo);
        });
        return new Result<>().success(activityVos);
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

    private String getActivityStatus(Integer status) {
        switch (status) {
            case 0:
                return "未审核";
            case 1:
                return "已通过";
            case -1:
                return "驳回";
            default:
                return "未知";
        }
    }
}
