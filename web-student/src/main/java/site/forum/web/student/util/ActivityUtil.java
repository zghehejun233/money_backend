package site.forum.web.student.util;

import org.springframework.stereotype.Component;
import site.forum.web.student.mapper.BaseActivityMapper;
import site.forum.web.common.data.po.BaseActivity;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@Component
public class ActivityUtil {
    @Resource
    private BaseActivityMapper baseActivityMapper;

    public void insertBaseActivity(BaseActivity activity, Long id) {
        baseActivityMapper.insert(activity);
        baseActivityMapper.insertRelation(id, activity.getId());
    }

    public Result<?> deleteBaseActivity(BaseActivity activity, Long id) {
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
}
