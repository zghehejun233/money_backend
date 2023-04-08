package site.forum.web.admin.service;

import org.springframework.stereotype.Service;
import site.forum.web.common.data.po.Period;
import site.forum.web.admin.mapper.SelectionMapper;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SelectionService {

    @Resource
    SelectionMapper selectionMapper;

    public Result<?> submitSelectionPeriod(String begin, String end, String name) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date b = dateFormat.parse(begin), e = dateFormat.parse(end);
            if (b.after(e)) return new Result<>().fail("结束时间不能晚于起始时间");
        } catch (ParseException ex) {
            return new Result<>().fail("时间格式错误！");
        }
        selectionMapper.insert(new Period(null, name, begin, end));
        return new Result<>().success();
    }

    public Result<?> getAllPeriod() {
        List<Period> periods = selectionMapper.selectList(null);
        return new Result<>().success(periods);
    }


}
