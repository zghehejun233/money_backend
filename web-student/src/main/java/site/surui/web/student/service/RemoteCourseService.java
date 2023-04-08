package site.surui.web.student.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.student.config.OpenFeignConfiguration;
import site.surui.web.student.data.po.Score;

import java.util.List;
import java.util.Map;

@FeignClient(value = "web-course", configuration = OpenFeignConfiguration.class)
@Headers("Content-Type:application/json")
public interface RemoteCourseService {

    @GetMapping(value = "/course/time")
    Result<Map<String, List<Integer>>> getCourseTime(@RequestParam("course") Long courseId,
                                                     @RequestParam("week") Integer week,
                                                     @RequestParam("start") Integer startWeek,
                                                     @RequestParam("end") Integer endWeek);

    @GetMapping("/score/")
    Result<List<Score>> getScoreByStudent(@RequestParam("id") String userId);

    @GetMapping("/select/course")
    Result<?> getPossibleCourses(@RequestParam("id") Long studentId,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "state", required = false) Integer state,
                                 @RequestParam(value = "low", required = false) Integer low,
                                 @RequestParam(value = "high", required = false) Integer high);

    @DeleteMapping("/select/")
    Result<?> quitCourse(@RequestParam("course") Long courseId,
                         @RequestParam("student") Long studentId);

    @PutMapping("/select/")
    Result<?> selectCourse(@RequestParam("course") Long courseId,
                                                 @RequestParam("student") Long studentId);
}
