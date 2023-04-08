package site.surui.web.student.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import site.surui.web.common.data.po.BaseActivity;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.common.data.vo.result.StudentInfoError;
import site.surui.web.student.mapper.BaseActivityMapper;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseActivityImpl implements BaseActivityService {
    @Resource
    private BaseActivityMapper baseActivityMapper;

    @Override
    public Result<?> getAllActivity(Long id) {
        List<BaseActivity> list = baseActivityMapper.findAllByStudent(id);

        if (list.isEmpty()) {
            return new Result<>().fail(StudentInfoError.INFO_NOT_FOUND);
        } else {
            return new Result<>().success(list);
        }
    }

    @Override
    public Result<?> addActivity(String userId, JSONObject object) {
        BaseActivity baseActivity = object.toJavaObject(BaseActivity.class);

        baseActivityMapper.insert(baseActivity);
        // baseActivityMapper.setSid(baseActivity.getId().toString(), userId);
        return new Result<>().fail("deprecated");
    }

    @Override
    public Result<?> getActivity(String userId, String name) {
        // BaseActivity activity = baseActivityMapper.findAll(userId, name);
        // if (activity == null) {
        //     return new Result().fail(StudentInfoError.INFO_NOT_FOUND);
        // } else
        //     return new Result().success(activity);
        return new Result<>().fail("deprecated");
    }

    @Override
    public Result<?> deleteActivity(String userId, String id) {
        BaseActivity activity = baseActivityMapper.findActivityById(id);
        if (activity == null) {
            return new Result<>().fail(StudentInfoError.FAIL_TO_DELETE);
        }
        int operatedRecords = baseActivityMapper.deleteByIdInt(activity.getId().toString());
        if (operatedRecords == 0) {
            return new Result<>().fail(StudentInfoError.FAIL_TO_DELETE);
        } else {
            return new Result<>().success();
        }

    }

    @Override
    public Result<?> editActivity(String userId, String id, JSONObject data) {
        boolean flag = false;

        Map<String, Object> params = new HashMap<>();
        getAllKey(data, params);

        BaseActivity activity = baseActivityMapper.findById(id);

        try {
            for (Field field : BaseActivity.class.getDeclaredFields()) {
                if (params.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(activity, params.get(field.getName()));
                    flag = true;
                }
            }
        } catch (IllegalAccessException accessException) {
            accessException.printStackTrace();
            return new Result<String>(StudentInfoError.FAIL_TO_EDIT_INFO);
        }

        baseActivityMapper.updateById(activity);

        return flag ? new Result<String>().success() : new Result<String>(StudentInfoError.FAIL_TO_EDIT_INFO);
    }

    private Boolean isJsonObject(String jsonString) {
        try {
            JSONObject.parseObject(JSON.parseObject(jsonString, String.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void getAllKey(JSONObject jsonObject, Map<String, Object> param) {
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String string = jsonObject.getString(String.valueOf(entry.getKey()));
            param.put(entry.getKey(), string);
            if (isJsonObject(string)) {
                JSONObject son = JSONObject.parseObject(JSON.parseObject(string, String.class));
                getAllKey(son, param);
            }
        }
    }

}
