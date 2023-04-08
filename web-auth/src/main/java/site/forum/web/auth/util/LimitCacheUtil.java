package site.forum.web.auth.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

public class LimitCacheUtil {
    private static final Long EXPIRE_TIME = 10 * 1000L;
    private static final Long CLEAN_TIME = 60 * 1000L;

    private static final TimedCache<String, Integer> timedCache = CacheUtil.newTimedCache(EXPIRE_TIME);

    //  启动定时任务
    static {
        timedCache.schedulePrune(CLEAN_TIME);
    }

    public static void put(String key, Integer value) {
        timedCache.put(key, value);
    }

    public static void put(String key, Integer value, Integer expireTime) {
        timedCache.put(key, value, expireTime);
    }

    public static Integer get(String key) {
        return timedCache.get(key);
    }

    public static void remove(String key) {
        timedCache.remove(key);
    }

    public static void clear() {
        timedCache.clear();
    }
}
