package site.forum.web.webcourse.service;

import cn.hutool.db.nosql.redis.RedisDS;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import site.forum.web.common.util.Base64Util;
import site.forum.web.webcourse.data.dto.SelectionEventDto;
import site.forum.web.webcourse.data.event.SelectionEvent;
import site.forum.web.webcourse.data.event.SelectionProcedureEvent;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Slf4j
public class SelectionEventListener {

    @Resource
    private ApplicationEventPublisher publisher;

    @EventListener(classes = {SelectionEvent.class})
    @Order(10)
    @Async
    public void eventListener(SelectionEvent event) {
        log.info("SelectionEvent: {}", event);
        log.info("SelectionEvent info: {}", event.getSelectionDto());
        SelectionEventDto selectionDto = event.getSelectionDto();
        Jedis jedis;
        try  {
            jedis = RedisDS.create().getJedis();
        } catch (JedisException jedisException) {
            log.error("SelectionEvent: {}", "Redis连接失败");
            log.error(jedisException.getMessage());
            return;
        }

        String SELECTION_KEY = "selection:";
        String key = SELECTION_KEY + selectionDto.getCourseId();
        String mem = Base64Util.encode(JSON.toJSONString(selectionDto));
        if (jedis.zrank(key, mem) != null) {
            log.info("SelectionEvent: {}", "已经存在");
        } else {
            jedis.zadd(key, new Date().getTime(), mem);
            log.info("SelectionEvent: {}", "添加成功");
            log.info("SelectionEvent: {}", "发布事件");
            publisher.publishEvent(new SelectionProcedureEvent(this, new Date(), selectionDto.getCourseId()));

        }
        jedis.close();
    }
}
