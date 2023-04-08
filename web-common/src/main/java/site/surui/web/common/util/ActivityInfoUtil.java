package site.surui.web.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import site.surui.web.common.data.po.BaseActivity;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class ActivityInfoUtil {

    private static final Map<String, Integer> statusMap = new HashMap<>();
    private static final Map<String, Integer> typeMap = new HashMap<>();
    private static final Map<String, Integer> socialTypeMap = new HashMap<>();

    static {
        statusMap.put("未处理", 0);
        statusMap.put("通过", 1);
        statusMap.put("拒绝", -1);

        typeMap.put("Undefined", -1);
        typeMap.put("社会实践", 0);
        typeMap.put("学生工作", 1);
        typeMap.put("志愿服务", 2);
        typeMap.put("学科竞赛", 3);
        typeMap.put("科研成果", 4);

        socialTypeMap.put("Undefined", 0);
    }

    public static int defaultStatusCode() {
        return 0;
    }

    public static float defaultScore() {
        return 0.0f;
    }

    public static <T> void initDefaultValue(BaseActivity activity, Long maintainerId, String type) {
        activity.setType(ActivityInfoUtil.type2Code(type));
        activity.setStatus(ActivityInfoUtil.defaultStatusCode());
        activity.setScore(defaultScore());
        activity.setMaintainer(maintainerId);
    }

    public static <T> BaseActivity activity2Base(T activity) {
        BaseActivity baseActivity = new BaseActivity();

        Map<String, Object> param = new HashMap<>();
        ReflectionUtil.getAllKey(JSONObject.parseObject(JSONObject.toJSONString(activity)), param);

        Stream.of(BaseActivity.class.getDeclaredFields())
                .filter(field -> param.containsKey(field.getName()))
                .forEach(field -> {
                            try {
                                setFieldByType(baseActivity, param, field);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );

        return baseActivity;
    }

    public static <T> T base2Activity(BaseActivity baseActivity, Class<T> clazz) {
        Constructor<T> constructor;
        T activity;
        try {
            constructor = clazz.getConstructor();
            activity = clazz.cast(constructor.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Object> param = new HashMap<>();
        ReflectionUtil.getAllKey(JSONObject.parseObject(JSONObject.toJSONString(baseActivity)), param);

        Stream.of(clazz.getDeclaredFields())
                .filter(field -> param.containsKey(field.getName()))
                .forEach(field -> {
                            log.info(field.getName() + " " + param.get(field.getName()));
                            try {
                                setFieldByType(activity, param, field);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );

        return activity;
    }

    public static void editActivity(JSONObject data, BaseActivity activity) {
        Map<String, Object> param = new HashMap<>();
        ReflectionUtil.getAllKey(data, param);

        Stream.of(activity.getClass().getDeclaredFields())
                .filter(field -> param.containsKey(field.getName()))
                .forEach(field -> {
                            try {
                                setFieldByType(activity, param, field);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

    private static <T> void setFieldByType(T activity, Map<String, Object> param, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        switch (field.getType().getName()) {
            case "java.lang.String":
                field.set(activity, String.valueOf(param.get(field.getName())));
                break;
            case "java.lang.Integer":
                field.set(activity, Integer.valueOf(String.valueOf(param.get(field.getName()))));
                break;
            case "java.lang.Float":
                field.set(activity, Float.valueOf(String.valueOf(param.get(field.getName()))));
                break;
            case "java.lang.Long":
                field.set(activity, Long.valueOf(String.valueOf(param.get(field.getName()))));
                break;
            default:
                throw new MethodNotFoundException("未找到对应的类型");
        }
    }


    public static String code2Status(int code) {
        for (String key : statusMap.keySet()) {
            if (statusMap.get(key).equals(code)) {
                return key;
            }
        }
        return null;
    }

    public static String status2Code(String status) {
        return statusMap.get(status).toString();
    }

    public static String code2Type(int code) {
        for (String key : typeMap.keySet()) {
            if (typeMap.get(key).equals(code)) {
                return key;
            }
        }
        return null;
    }

    public static int type2Code(String type) {
        try {
            return typeMap.get(type);
        } catch (NullPointerException e) {
            log.warn(e.getMessage());
            log.warn("Trying to get type code of " + type);
            return -1;
        }
    }

    public static int socialType2Code(String type) {
        try {
            return socialTypeMap.get(type);
        } catch (NullPointerException e) {
            log.warn(e.getMessage());
            log.warn("Trying to get type code of " + type);
            return -1;
        }
    }

    public static String code2SocialType(int code) {
        for (String key : socialTypeMap.keySet()) {
            if (socialTypeMap.get(key).equals(code)) {
                return key;
            }
        }
        return null;
    }
}
