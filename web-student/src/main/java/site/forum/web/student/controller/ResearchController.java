package site.forum.web.student.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import site.forum.web.student.service.ActivityService;
import site.forum.web.common.data.po.User;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.student.data.vo.ResearchVo;
import site.forum.web.student.http.StudentInfo;

import javax.annotation.Resource;

@RestController
@RequestMapping("/activity/research")
public class ResearchController {
    private static final String TYPE = "科研成果";

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    Result<?> addResearch(@StudentInfo User user,
                          @RequestBody ResearchVo researchVo) {
        return activityService.addActivity(user.getId(), TYPE, researchVo,null);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    Result<?> deleteResearch(@StudentInfo User user,
                             @RequestParam("id") Long id) {
        return activityService.deleteActivity(user.getId(), id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Result<?> editResearch(@StudentInfo User user,
                           @RequestParam("id") Long id,
                           @RequestBody JSONObject data) {
        return activityService.editActivity(user.getId(), id, data);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Result<?> getAllResearch(@StudentInfo User user) {
        return activityService.getActivity(user.getId(), TYPE, ResearchVo.class);
    }
}
