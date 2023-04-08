package site.forum.web.webcourse.service;

import cn.hutool.db.nosql.redis.RedisDS;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import site.forum.web.common.util.Base64Util;
import site.forum.web.webcourse.data.dto.SelectionEventDto;
import site.forum.web.webcourse.data.event.SelectionProcedureEvent;
import site.forum.web.webcourse.mapper.SelectionMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@EnableScheduling
public class SelectionProcedureEventListener {
    private Date lastActionTime;
    private final List<Long> courseIds;
    private final List<Long> workingCourseIds;
    private static final long minDuration = 1000 * 60;
    private static final int maxThreadNum = 50;
    private int usableThreadNum = maxThreadNum;
    @Resource
    private SelectionMapper selectionMapper;


    public SelectionProcedureEventListener() {
        this.courseIds = new ArrayList<>();
        this.workingCourseIds = new ArrayList<>();
        this.lastActionTime = new Date();
    }

    @EventListener(classes = {SelectionProcedureEvent.class})
    @Order(50)
    @Async
    public void eventListener(SelectionProcedureEvent event) {
        if (workingCourseIds.contains(event.getCourseId())) {
            log.info("SelectionProcedureEvent: {}", "正在处理中");
            return;
        }
        if (!courseIds.contains(event.getCourseId())) {
            synchronized (courseIds) {
                courseIds.add(event.getCourseId());
            }
        }

        // if ((event.getTime().getTime() - lastActionTime.getTime() < minDuration)) {
        //     log.info("SelectionProcedureEvent: {}", "触发过于频繁");
        // } else {
            activateWorkings(new Date());
        // }

    }

    // @Scheduled(cron = "*/60 * * * * ?")
    @Scheduled(cron = "*/15 * * * * ?")
    public void scheduledRefresh() {
        log.warn("ThreadNum: {}", usableThreadNum);
        log.info("SelectionProcedureEvent: {}", "定时刷新");
        Jedis jedis = RedisDS.create().getJedis();
        Set<String> exists = jedis.keys("selection:*");
        log.warn("SelectionProcedureEvent: {}", exists);
        jedis.close();

        exists.forEach(key -> {
            String[] split = key.split(":");
            Long courseId = Long.parseLong(split[1]);
            if (!courseIds.contains(courseId)) {
                synchronized (courseIds) {
                    courseIds.add(courseId);
                }
            }
        });
        log.info("new courseIds: {}", courseIds);
        activateWorkings(new Date());
        log.info("SelectionProcedureEvent: {}", "定时刷新结束");
    }


    private void activateWorkings(Date time) {
        log.info("SelectionProcedureEvent: {}", "触发成功，添加新的课程处理线程");
        log.warn(courseIds.toString());
        lastActionTime = time;
        courseIds.stream()
                .filter(id -> !workingCourseIds.contains(id))
                .limit(usableThreadNum)
                .forEach(id -> {
                    WorkingThread workingThread = new WorkingThread(id);
                    workingThread.start();

                    workingCourseIds.add(id);
                    usableThreadNum--;

                    log.info("SelectionProcedureEvent: {}", "开始处理");
                });
    }

    private void work(Long courseId) {
        log.info("SelectionProcedureEvent: {}", "执行选课处理"); // 模拟选课处理

        String SELECTION_KEY = "selection:";
        String key = SELECTION_KEY + courseId;
        Jedis jedis = RedisDS.create().getJedis();
        log.info(jedis.zrange(key, 0, -1).toString());
        Set<String> studentsInCourse = jedis.zrange(key, 0, -1);
        jedis.close();

        List<String> insertedMem = new ArrayList<>();

        studentsInCourse
                .forEach(student -> {
                            SelectionEventDto selectionEventDto = JSONObject.parseObject(Base64Util.decode(student), SelectionEventDto.class);
                            log.info("SelectionProcedureEvent: {}", selectionEventDto.toString());
                            Long selectionId = selectionMapper.isSelected(courseId, selectionEventDto.getStudentId());
                            log.warn("SelectionProcedureEvent: {}", selectionId);
                            if (selectionId == null || selectionId == 0) {
                                try {
                                    // SelectionDto selectionDto = new SelectionDto(null, selectionEventDto.getCourseId(), selectionEventDto.getStudentId());
                                    selectionMapper.insertSelection(selectionEventDto.getCourseId(), selectionEventDto.getStudentId());
                                } catch (Exception e) {
                                    log.warn("SelectionProcedureEvent: {}", "选课失败，学生或课程不存在");
                                    e.printStackTrace();
                                }
                                insertedMem.add(student);
                                log.info("Success: {}", selectionEventDto);

                            } else {
                                log.warn("SelectionProcedureEvent: {}", "选课失败，学生已选该课程");
                                insertedMem.add(student);
                            }
                        }
                );

        usableThreadNum++;
        jedis = RedisDS.create().getJedis();
        try {
            for (String s : insertedMem) {
                jedis.zrem(key, s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        synchronized (courseIds) {
            courseIds.remove(courseId);
        }
        synchronized (workingCourseIds) {
            workingCourseIds.remove(courseId);
        }

        log.info("SelectionProcedureEvent: {}", "执行选课过程结束");
    }

    class WorkingThread extends Thread {
        private final long courseId;
        public volatile boolean exit = false;

        public WorkingThread(long courseId) {
            this.courseId = courseId;
        }

        @Override
        public void run() {
            work(courseId);

        }
    }
}
