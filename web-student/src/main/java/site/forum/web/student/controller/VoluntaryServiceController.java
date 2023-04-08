package site.forum.web.student.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import site.forum.web.student.data.vo.VoluntaryVo;
import site.forum.web.student.http.StudentInfo;
import site.forum.web.student.service.ActivityService;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activity/voluntary")
public class VoluntaryServiceController {
    private static final String TYPE = "志愿服务";

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    Result<?> addVoluntary(@StudentInfo User user,
                           @RequestParam(value = "pid", required = false) String participantIds,
                           @RequestBody VoluntaryVo voluntaryVo) {
        return activityService.addActivity(user.getId(), TYPE, voluntaryVo,participantIds);
    }

    @RequestMapping(value = "/participate", method = RequestMethod.PUT)
    Result<?> addParticipate(@StudentInfo User user,
                             @RequestParam("id") Long id,
                             @RequestBody List<Long> participateIds) {
        return activityService.addParticipants(user.getId(), id, participateIds);
    }

    @RequestMapping(value = "/participate", method = RequestMethod.DELETE)
    Result<?> deleteParticipate(@StudentInfo User user,
                                @RequestParam("id") Long id,
                                @RequestBody List<Long> participateIds) {
        return activityService.deleteParticipants(user.getId(), id, participateIds);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    Result<?> deleteVoluntary(@StudentInfo User user,
                              @RequestParam("id") Long id) {
        return activityService.deleteActivity(user.getId(), id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Result<?> editVoluntary(@StudentInfo User user,
                            @RequestParam("id") Long id,
                            @RequestBody JSONObject data) {
        return activityService.editActivity(user.getId(), id, data);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Result<?> getAllVoluntary(@StudentInfo User user) {
        return activityService.getActivity(user.getId(), TYPE, VoluntaryVo.class);
    }
}
