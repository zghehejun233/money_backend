package site.forum.web.student.service;

import com.alibaba.fastjson.JSONObject;
import site.forum.web.common.data.vo.result.Result;


public interface BaseActivityService {


    Result<?> getAllActivity(Long id);


    Result<?> addActivity(String userId, JSONObject object);

    Result<?> getActivity(String userId, String name);

    Result<?> deleteActivity(String userId, String id);

    Result<?> editActivity(String userId, String id, JSONObject data);
}
