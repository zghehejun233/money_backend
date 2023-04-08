package site.forum.web.admin.service;

import cn.hutool.db.nosql.redis.RedisDS;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import site.forum.web.admin.data.dto.SystemScaleInfoDto;
import site.forum.web.admin.mapper.SystemInfoMapper;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
@EnableScheduling
@RefreshScope
public class SystemInfoService implements ApplicationListener<ApplicationStartedEvent> {
    private Jedis jedis;

    @Resource
    private SystemInfoMapper systemInfoMapper;

    private final String SCALE_INFO = "scale:info";

    public SystemInfoService() {
        this.jedis = RedisDS.create().getJedis();
    }

    @Override
    public void onApplicationEvent(@Nonnull ApplicationStartedEvent event) {
        jedis = RedisDS.create().getJedis();
        log.info("Starting to initialize system information...");
        if (jedis == null) {
            log.error("Redis connection failed, please check the configuration");
            return;
        }
        log.debug(jedis.info());

        // check redis connection
        if (jedis.isConnected()) {
            log.info("Redis connection successful");
        } else {
            log.error("Redis connection failed, please check the configuration");
        }
        jedis.close();
        initRedis();

        updateScaleInformation();
    }


    public Result<?> getSystemScale(Integer length) {
        if (length == null || length == 0) {
            length = -1;
        }
        jedis = RedisDS.create().getJedis();
        Set<Tuple> tuples = jedis.zrevrangeWithScores(SCALE_INFO, 0, length);
        jedis.close();
        List<SystemScaleInfoDto> scaleInfoDtoList = new ArrayList<>();
        tuples.stream()
                .sorted(Comparator.comparing(Tuple::getScore).reversed())
                .forEach(tuple -> {
                    SystemScaleInfoDto scaleInfoDto = JSON.parseObject(tuple.getElement(), SystemScaleInfoDto.class);
                    scaleInfoDtoList.add(scaleInfoDto);
                });
        return (scaleInfoDtoList.isEmpty()) ? new Result<>().fail() : new Result<>().success(scaleInfoDtoList);
    }

    public Result<?> forceClearCache() {
        log.warn("Force clearing cache...");
        jedis = RedisDS.create().getJedis();
        jedis.del(SCALE_INFO);
        Map<String, Double> map = new HashMap<>();
        map.put("", (double) new Date().getTime());
        jedis.zadd(SCALE_INFO, map);
        log.warn("Cache cleared");
        jedis.close();
        return new Result<>().success();
    }

    @Scheduled(cron = "*/60 * * * * ?")
    private void updateScaleInformation() {
        log.debug("Updating scale information: {}", SCALE_INFO);
        jedis = RedisDS.create().getJedis();
        Date currentTime = new Date();
        SystemScaleInfoDto systemScaleInfoDto =
                new SystemScaleInfoDto(
                        systemInfoMapper.getStudentScale(),
                        systemInfoMapper.getAdminScale(),
                        systemInfoMapper.getTeacherScale(),
                        currentTime);
        Map<String, Double> map = new HashMap<>();
        map.put(JSON.toJSONString(systemScaleInfoDto), (double) currentTime.getTime());
        jedis.zadd(SCALE_INFO, map);
        jedis.close();
        log.debug("Scale information updated: {}", SCALE_INFO);
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void clearCache() {
        log.info("Clearing cache...");
        log.info("Not implemented yet");
    }

    private void initRedis() {
        jedis = RedisDS.create().getJedis();
        if (!jedis.exists(SCALE_INFO)) {
            log.warn("Scale {} does not exist, initializing...", SCALE_INFO);
            Map<String, Double> map = new HashMap<>();
            map.put("", (double) new Date().getTime());
            jedis.zadd(SCALE_INFO, map);
            log.info("Scale {} initialized successfully", SCALE_INFO);
        }
        jedis.close();
    }
}
