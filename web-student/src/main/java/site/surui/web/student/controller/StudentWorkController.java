package site.surui.web.student.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.student.data.vo.StudentWorkVo;
import site.surui.web.student.http.StudentInfo;
import site.surui.web.student.service.ActivityService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/activity/work")
public class StudentWorkController {
    private static final String TYPE = "学生工作";

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    Result<?> addStudentWork(@StudentInfo User user,
                             @RequestBody StudentWorkVo studentWorkVo) {
        return activityService.addActivity(user.getId(), TYPE, studentWorkVo, null);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    Result<?> deleteStudentWork(@StudentInfo User user,
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
        return activityService.getActivity(user.getId(), TYPE, StudentWorkVo.class);
    }
}

