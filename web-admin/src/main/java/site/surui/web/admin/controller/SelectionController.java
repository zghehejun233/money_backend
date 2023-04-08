package site.surui.web.admin.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.surui.web.admin.service.SelectionService;
import site.surui.web.common.data.vo.result.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("/select")
public class SelectionController {

    @Resource
    SelectionService selectionService;

    @ApiOperation(value = "提交选课时间起止时间")
    @RequestMapping(value = "/submit", method = RequestMethod.PUT)
    public Result<?> submitSelectionPeriod(@RequestParam String begin, @RequestParam String end, @RequestParam String name) {
        return selectionService.submitSelectionPeriod(begin, end, name);
    }

    @ApiOperation(value = "获取所有选课轮次")
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public Result<?> getAllPeriod() {
        return selectionService.getAllPeriod();
    }


}
