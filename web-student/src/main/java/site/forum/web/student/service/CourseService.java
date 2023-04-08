package site.forum.web.student.service;

import site.forum.web.common.data.vo.result.Result;

public interface CourseService {

    Result<?> getAllCourses(Long id);

    Result<?> getSchedule(Long id, Integer start, Integer end);

    Result<?> getExams(Long studentId);
}

