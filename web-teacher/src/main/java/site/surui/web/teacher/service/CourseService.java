package site.surui.web.teacher.service;

import com.alibaba.fastjson.JSONObject;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.po.Student;
import site.surui.web.common.data.vo.StudentWithScore;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.teacher.config.OpenFeignConfiguration;
import site.surui.web.common.data.po.Score;

import java.util.List;
import java.util.Map;

@FeignClient(value = "web-course",configuration = OpenFeignConfiguration.class)
@Headers("Content-Type: application/json")
public interface CourseService {
    @GetMapping("/course/")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Result<List<CourseInfo>> getCourse(@RequestParam("name") String name,
                   @RequestParam("teacher") String teacher,
                   @RequestParam("teacherId") Long teacherId,
                   @RequestParam("id") String courseId);

    @GetMapping("/course/")
    List<CourseInfo> getCourse2(@RequestParam("name") String name,
                               @RequestParam("teacher") String teacher,
                               @RequestParam("teacherId") Long teacherId,
                               @RequestParam("id") String courseId);

    @PutMapping("/course/")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Result<String> addCourse(@RequestBody CourseInfo courseInfo);

    @PostMapping("/course/")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Result<String> editCourseInfo(@RequestParam("id") Long id, @RequestBody JSONObject data);

    @GetMapping("/course/student/")
    Result<List<StudentWithScore>> getStudentWithScoreList(@RequestParam("courseId") Long courseId);

    @DeleteMapping("/course/")
    Result<?> deleteCourse(@RequestParam("id") Long id);

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
                           @RequestParam("student") Long studentId);}
