package site.surui.web.webcourse.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.webcourse.data.po.Score;
import site.surui.web.webcourse.service.ScoreService;

import javax.annotation.Resource;

@RestController
@Api("成绩信息接口")
@RequestMapping("/score")
public class ScoreController {
    @Resource
    private ScoreService scoreService;

    @ApiOperation(value = "获取指定学生成绩", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Result<?> getScoreByStudent(@RequestParam("id") @ApiParam("学号") String userId) {
        return scoreService.getScore(userId);
    }

    @ApiOperation(value = "录入学生成绩", notes = "已测试")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Result<?> setScore(@RequestParam String studentId, @RequestBody Score score) {
        return scoreService.setScore(studentId, score);
    }

    @ApiOperation(value = "修改学生成绩", notes = "已测试")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<?> editScore(@RequestParam("id") @ApiParam("要修改的成绩记录id") Long id,
                            @RequestBody JSONObject data) {
        return scoreService.editScore(id, data);
    }

    @ApiOperation(value = "改变成绩状态成绩", notes = "已测试")
    @RequestMapping(value = "/lock", method = RequestMethod.GET)
    public Result<?> lockScore(@RequestParam("id") @ApiParam("要修改的成绩记录id") Long id,
                            @RequestParam("status") @ApiParam("要修改的成绩记录id") boolean status) {
        return scoreService.changeScoreStatus(id, status);
    }

    @ApiOperation(value = "获取课程的所有成绩")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public Result<?> getAllScoresByCourse(@RequestParam("course") Long id) {
        return scoreService.getAllScores(id);
    }

    @ApiOperation("更新成绩排名")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Result<?> update(@RequestParam("id") Long id) {
        return scoreService.updateScores(id);
    }

    @ApiOperation("更新总成绩和排名")
    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public Result<?> updateTotal() {
        return scoreService.updateAllScores();
    }

}
