package site.forum.web.webcourse.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.webcourse.data.dto.UpdateScoreDto;
import site.forum.web.webcourse.data.event.UpdateScoreEvent;
import site.forum.web.webcourse.data.po.Score;
import site.forum.web.webcourse.data.po.TotalScore;
import site.forum.web.webcourse.mapper.CourseInfoMapper;
import site.forum.web.webcourse.mapper.ScoreMapper;
import site.forum.web.webcourse.mapper.StudentMapper;
import site.forum.web.webcourse.mapper.TotalScoreMapper;
import site.forum.web.webcourse.util.ReflectionUtil;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ScoreService {
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CourseInfoMapper courseInfoMapper;
    @Resource
    private TotalScoreMapper totalScoreMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public Result<?> getScore(String studentId) {
        List<Score> scores = scoreMapper.findAllStudentWithScore(studentId);
        applicationEventPublisher.publishEvent(new UpdateScoreEvent(this, new UpdateScoreDto(new Date(System.currentTimeMillis()), 3)));
        return (scores.isEmpty()) ? new Result<>().fail() : new Result<>().success(scores);
    }

    public Result<?> setScore(String userId, Score score) {
        scoreMapper.insert(score);
        if (score.getId() == null) {
            return new Result<>().fail();
        } else if (score.isLock()) {
            return new Result<>().fail();
        }
        Long id = courseInfoMapper.findIdByStudentId(userId);
        scoreMapper.setSid(score.getId(), id);
        return new Result<>().success();
    }


    public Result<?> editScore(Long id, JSONObject data) {
        Score score = scoreMapper.selectById(id);
        if (score == null) {
            return new Result<>().fail();
        } else if (score.isLock()) {
            return new Result<>().fail();
        }

        boolean flag = false;
        Map<String, Object> params = new HashMap<>();
        ReflectionUtil.getAllKey(data, params);

        try {
            for (Field field : Score.class.getDeclaredFields()) {
                if (params.containsKey(field.getName())) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (type.isAssignableFrom(Float.class)) {
                        field.set(score, Float.valueOf((String) params.get(field.getName())));
                        flag = true;
                    } else if (type.isAssignableFrom(Integer.class)) {
                        field.set(score, Integer.valueOf((String) params.get(field.getName())));
                        flag = true;
                    } else {
                        try {
                            field.set(score, params.get(field.getName()));
                            flag = true;
                        } catch (IllegalArgumentException illegalArgumentException) {
                            illegalArgumentException.printStackTrace();
                            return new Result<>().fail();
                        }
                    }
                }
            }
        } catch (IllegalAccessException accessException) {
            accessException.printStackTrace();
            return new Result<>().fail();
        }

        scoreMapper.updateById(score);
        return flag ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> changeScoreStatus(Long id, boolean status) {

        Score score = scoreMapper.selectById(id);
        if (score == null) {
            return new Result<>().fail();
        }
        score.setLock(status);
        return (scoreMapper.updateById(score) == 1) ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> getAllScores(Long id) {
        List<Score> scores = scoreMapper.findAllByCid(id);

        return (scores.isEmpty()) ? new Result<>().fail() : new Result<>().success(scores);
    }

    public Result<?> updateScores(Long courseId) {
        List<Score> scores = scoreMapper.findAllByCid(courseId);
        // 排序赋排名
        scores.sort(Comparator.comparing(Score::getTotal));
        AtomicReference<Integer> length = new AtomicReference<>(scores.size());
        scores.forEach(score -> score.setRank(length.getAndSet(length.get() - 1)));
        // 写入排名
        scores.forEach(score -> scoreMapper.updateRank(score.getRank(), score.getId()));
        return new Result<>().success();
    }

    public Result<?> updateAllScores() {
        List<Score> allWrittenScores = studentMapper.selectAll();
        List<Student> students = new ArrayList<>();
        // 获取所有有成绩记录的学生
        allWrittenScores.forEach(score -> {
            Student student = studentMapper.selectById(score.getSid());
            students.add(student);
        });


        students.forEach(student -> {
            // 获取该学生的所有成绩记录
            List<Score> scores = scoreMapper.findALlBySid(student.getId());

            // 记录学生的总学分
            AtomicReference<Float> studentTotalCredit = new AtomicReference<>(0.0f);
            scores.forEach(score -> studentTotalCredit.updateAndGet(v -> v + courseInfoMapper.selectById(score.getCid()).getCredit()));

            AtomicReference<Float> averageScore = new AtomicReference<>(0.0f);
            scores.forEach(score -> averageScore.updateAndGet(v -> v + score.getTotal() * (courseInfoMapper.selectById(score.getCid()).getCredit() / studentTotalCredit.get())));

            TotalScore scoreData = totalScoreMapper.selectBySid(student.getId());
            if (scoreData == null) {
                scoreData = new TotalScore(null, student.getId(), null, averageScore.get());
                totalScoreMapper.insertByHand(scoreData.getSid(), scoreData.getTotalScore());
            } else {
                scoreData.setTotalScore(averageScore.get());
                totalScoreMapper.updateById(scoreData);
            }
        });

        List<TotalScore> totalScores = totalScoreMapper.selectAll();
        totalScores.sort(Comparator.comparing(TotalScore::getTotalScore));
        AtomicReference<Integer> length = new AtomicReference<>(totalScores.size());
        totalScores.forEach(totalScore -> totalScore.setTotalRank(length.getAndSet(length.get() - 1)));
        totalScores.forEach(totalScore -> totalScoreMapper.updateRank(totalScore.getTotalRank(), totalScore.getId()));

        return new Result<>().success();
    }
}

