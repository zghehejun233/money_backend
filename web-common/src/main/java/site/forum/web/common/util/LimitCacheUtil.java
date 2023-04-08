package site.forum.web.common.util;

import cn.hutool.db.nosql.redis.RedisDS;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class LimitCacheUtil {
    private static final String ACCESS_PREFIX = "access:";
    private static final Jedis jedis = RedisDS.create().getJedis();

    public static Integer getRequestCount(String key, Integer seconds) {
        key = ACCESS_PREFIX + key;
        String count = jedis.get(key);
        if (count == null) {
            jedis.set(key, "1");
            jedis.expire(key, seconds);
            return 1;
        }
        jedis.incr(key);
        count = jedis.get(key);

        return Integer.parseInt(count);
    }
}
