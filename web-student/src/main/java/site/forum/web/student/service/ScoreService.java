package site.forum.web.student.service;

import site.forum.web.common.data.vo.result.Result;


public interface ScoreService {

    /**
     * 获取一个学生的所有成绩
     *
     * @param userId 学号
     * @return x
     */
    Result<?> getAllScores(String userId);

    /**
     * 根据条件获取指定成绩
     *
     * @param name 课程名
     * @return x
     */
    Result<?> getScore(String userId, String name);
}
