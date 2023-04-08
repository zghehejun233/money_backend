package site.forum.web.student.service;

import org.springframework.stereotype.Service;
import site.forum.web.common.data.po.Student;
import site.forum.web.common.data.vo.result.AuthError;
import site.forum.web.common.data.vo.result.Result;
import site.forum.web.student.data.dto.BlogDto;
import site.forum.web.student.data.po.Blog;
import site.forum.web.student.data.vo.BlogVo;
import site.forum.web.student.mapper.BlogMapper;
import site.forum.web.student.mapper.StudentMapper;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {

    @Resource
    BlogMapper blogMapper;
    @Resource
    StudentMapper studentMapper;

    public Result<?> getAllBlogs() {
        List<BlogVo> blogList = blogMapper.findAllBlogs();
        return new Result<>().success(blogList);
    }

    public Result<?> publishBlog(String userId, BlogDto blogDto) {
        Student student = studentMapper.getStudentInfo(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 8 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Blog blog = new Blog(null, blogDto.getTitle(), blogDto.getContent(), student.getId(), dateFormat.format(date));
        blogMapper.insert(blog);
        return new Result<>().success("博客发布成功");
    }

    public Result<?> editBlog(Long bid, String userId, BlogDto blogDto) {
        Student student = studentMapper.getStudentInfo(userId);
        Blog blog = blogMapper.selectById(bid);
        if (!Objects.equals(blog.getSid(), student.getId())) return new Result<String>(AuthError.PERM_NOT_ALLOW);
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blogMapper.updateById(blog);
        return new Result<>().success("博客保存成功");
    }

    public Result<?> deleteBlog(Long bid, Long userId) {
        Blog blog = blogMapper.selectById(bid);
        if (!Objects.equals(blog.getSid(), userId)) return new Result<String>(AuthError.PERM_NOT_ALLOW);
        blogMapper.deleteById(bid);
        return new Result<>().success("博客删除成功");
    }

    public Result<?> searchBlogs(Long bid, String name, String title, String content) {
        List<BlogVo> blogList = blogMapper.findByIdNameTitleContent(bid, name, title, content);

        return new Result<>().success(blogList);
    }
}
