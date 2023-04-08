package site.surui.web.student.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.student.data.vo.SocialPracticeVo;
import site.surui.web.student.http.StudentInfo;
import site.surui.web.student.service.ActivityService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activity/social")
public class SocialPracticeController {
    private static final String TYPE = "社会实践";

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    Result<?> addSocialPractice(@StudentInfo User user,
                                @RequestParam(value = "pid", required = false) String participantIds,
                                @RequestBody SocialPracticeVo socialPracticeVo) {
        return activityService.addActivity(user.getId(), TYPE, socialPracticeVo, participantIds);
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
    Result<?> deleteSocialPractice(@StudentInfo User user,
                                   @RequestParam("id") Long id) {
        return activityService.deleteActivity(user.getId(), id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Result<?> editSocialPractice(@StudentInfo User user,
                                 @RequestParam("id") Long id,
                                 @RequestBody JSONObject data) {
        return activityService.editActivity(user.getId(), id, data);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Result<?> getAllSocialPractice(@StudentInfo User user) {
        return activityService.getActivity(user.getId(), TYPE, SocialPracticeVo.class);
    }
}
