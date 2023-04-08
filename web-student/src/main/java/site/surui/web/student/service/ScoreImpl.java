package site.surui.web.student.service;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import site.surui.web.common.data.po.CourseInfo;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.data.vo.result.StudentInfoError;
import site.surui.web.student.data.po.Score;
import site.surui.web.student.data.po.TotalScore;
import site.surui.web.student.data.vo.ScoreVo;
import site.surui.web.student.mapper.CourseMapper;
import site.surui.web.student.mapper.ScoreMapper;
import site.surui.web.student.mapper.TotalScoreMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ScoreImpl implements ScoreService {
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private TotalScoreMapper totalScoreMapper;

    @Override
    public Result<?> getAllScores(String userId) {
        List<Score> scoreList = scoreMapper.findScoresByStudentId(userId);
        if (scoreList.isEmpty()) {
            return new Result<StudentInfoError>().fail(StudentInfoError.INFO_NOT_FOUND);
        }

        List<ScoreVo> scoreVos = new ArrayList<>(scoreList.size());
        for (Score score : scoreList) {
            CourseInfo courseInfo = courseMapper.findById(score.getCid());
            ScoreVo scoreVo = buildScoreVo(courseInfo, score);
            scoreVos.add(scoreVo);
        }
        TotalScore totalScore = totalScoreMapper.selectAllByUserId(userId);

        HashMap<String, Object> result = new HashMap<>();
        result.put("data", scoreVos);
        result.put("totalScore", limitFloat(totalScore.getTotalScore()));
        result.put("totalRank", totalScore.getTotalRank());
        return new Result<HashMap<String, Object>>().success(result);
    }

    @Override
    public Result<?> getScore(String userId, String courseId) {
        CourseInfo targetCourseInfo = courseMapper.findByCourseId(courseId);

        Score score;
        try {
            score = scoreMapper.findScoreByStudentAndCourse(userId, targetCourseInfo.getId());
        } catch (TooManyResultsException tooManyResultsException) {
            tooManyResultsException.printStackTrace();
            return new Result<String>().fail();
        }
        if (score == null) {
            return new Result<StudentInfoError>().fail(StudentInfoError.INFO_NOT_FOUND);
        }

        ScoreVo scoreVo = buildScoreVo(targetCourseInfo, score);
        return new Result<ScoreVo>().success(scoreVo);
    }

    private float limitFloat(float f) {
        return BigDecimal.valueOf(f).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    private ScoreVo buildScoreVo(CourseInfo courseInfo, Score score) {
        ScoreVo scoreVo = new ScoreVo();
        scoreVo.setName(courseInfo.getName());
        scoreVo.setNumber(courseInfo.getCourseId());
        scoreVo.setDaily(limitFloat(score.getUsual()));
        scoreVo.setExam(limitFloat(score.getExam()));
        scoreVo.setTotal(limitFloat(score.getTotal()));
        scoreVo.setWeight(limitFloat(courseInfo.getCredit()));
        scoreVo.setRank(score.getRank());

        return scoreVo;
    }
}
