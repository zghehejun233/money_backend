package site.forum.web.student.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.student.data.po.Comment;
import site.forum.web.student.data.vo.CommentVo;
import site.forum.web.student.mapper.CommentMapper;
import site.forum.web.student.mapper.StudentMapper;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private StudentMapper studentMapper;

    public Result<?> addComment(Long commenterId, Long studentId, JSONObject data) {
        String title = data.getString("title");
        String nickname = data.getString("nickname");
        String content = data.getString("content");
        Date date = new Date();
        date.setTime(date.getTime() + 8 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Comment comment = new Comment(null, studentId, title, nickname, content, dateFormat.format(date), commenterId);
        commentMapper.insert(comment);

        return (comment.getId() == null) ?
                new Result<>().fail() : new Result<>().success(comment.getId());
    }

    public Result<?> getComments(Long id) {
        List<Comment> commentList = commentMapper.findAllComments(id);
        List<CommentVo> vos = new ArrayList<>();
        commentList.forEach(comment -> {
            CommentVo vo = new CommentVo(
                    comment.getTitle(),
                    studentMapper.getStudentName(comment.getSid()),
                    comment.getContent(),
                    studentMapper.getAvatar(comment.getSid()),
                    comment.getDate());
            vos.add(vo);
        });

        return (commentList.isEmpty()) ? new Result<>().fail() : new Result<>().success(vos);
    }

    /**
     * 推送互评任务
     * <p>
     * 这个功能依赖一个定时事务，需要定时同步更新需要互评的同学的列表
     *
     * @param id xx
     * @return xx
     */
    public Result<?> pushCommentTasks(Long id) {
        int leastComments = 6;
        List<Student> students = commentMapper.findAllUnsatisfiedComments(id);
        students.forEach(student -> {
            if (commentMapper.countComments(student.getId()) >= leastComments) {
                students.remove(student);
            }
        });

        return (students.isEmpty()) ? new Result<>().fail() : new Result<>().success(students);
    }


    public void updateCommentTaskList(String classId) {

    }
}
