package site.forum.web.student.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import site.forum.web.student.data.vo.CompetitionVo;
import site.forum.web.student.http.StudentInfo;
import site.forum.web.student.service.ActivityService;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("/activity/competition")
public class CompetitionController {
    private static final String TYPE = "学科竞赛";

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    Result<?> addCompetition(@StudentInfo User user,
                             @RequestBody CompetitionVo socialPracticeVo) {
        return activityService.addActivity(user.getId(), TYPE, socialPracticeVo, null);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    Result<?> deleteCompetition(@StudentInfo User user,
                                @RequestParam("id") Long id) {
        return activityService.deleteActivity(user.getId(), id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Result<?> editCompetition(@StudentInfo User user,
                              @RequestParam("id") Long id,
                              @RequestBody JSONObject data) {
        return activityService.editActivity(user.getId(), id, data);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Result<?> getAllCompetition(@StudentInfo User user) {
        return activityService.getActivity(user.getId(), TYPE, CompetitionVo.class);
    }
}
