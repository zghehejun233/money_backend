package site.surui.web.student.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.student.data.dto.BlogDto;
import site.surui.web.student.http.StudentInfo;
import site.surui.web.student.service.BlogService;

import javax.annotation.Resource;

@RestController
@Api("学生学业信息")
@RequestMapping("/info")
@RefreshScope
public class BlogController {

    @Resource
    BlogService blogService;

    @ApiOperation(value = "按时间顺序获取所有博客", notes = "---已测试---\n")
    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public Result<?> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @ApiOperation(value = "搜索博客", notes = "---已测试---\n")
    @RequestMapping(value = "/blog/search", method = RequestMethod.GET)
    public Result<?> searchBlogs(@RequestParam(value = "bid", required = false) Long bid,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "title", required = false) String title,
                                 @RequestParam(value = "content", required = false) String content) {
        return blogService.searchBlogs(bid, name, title, content);
    }

    @ApiOperation(value = "发布一条博客", notes = "---已测试---\n")
    @RequestMapping(value = "/blog/publish", method = RequestMethod.POST)
    public Result<?> publishBlog(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo,
                                 @RequestBody @ApiParam("博客标题及内容") BlogDto blogDto) {
        return blogService.publishBlog(userInfo.getUserId(), blogDto);
    }

    @ApiOperation(value = "编辑一条博客", notes = "---已测试---\n")
    @RequestMapping(value = "/blog/edit/{bid}", method = RequestMethod.POST)
    public Result<?> editBlog(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo,
                              @PathVariable(name = "bid") Long bid,
                              @RequestBody @ApiParam("博客标题及内容") BlogDto blogDto) {
        return blogService.editBlog(bid, userInfo.getUserId(), blogDto);
    }

    @ApiOperation(value = "删除一条博客", notes = "---已测试---\n")
    @RequestMapping(value = "/blog/delete/{bid}", method = RequestMethod.DELETE)
    public Result<?> deleteBlog(@StudentInfo @ApiParam("用户信息，自动注入") User userInfo,
                                @PathVariable(name = "bid") Long bid) {
        return blogService.deleteBlog(bid, userInfo.getId());
    }
}
