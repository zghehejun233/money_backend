package site.surui.web.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class ReflectionUtil {
    public static Boolean isJsonObject(String jsonString) {
        try {
            JSONObject.parseObject(JSON.parseObject(jsonString, String.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void getAllKey(JSONObject jsonObject, Map<String, Object> param) {
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
