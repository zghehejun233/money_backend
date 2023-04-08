package site.surui.web.webcourse.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.webcourse.data.dto.UpdateScoreDto;
import site.surui.web.webcourse.data.event.UpdateScoreEvent;

import javax.annotation.Resource;


@Component
@AllArgsConstructor
@RefreshScope
public class UpdateScoreEventListener {
    private static final Logger logger = LoggerFactory.getLogger(UpdateScoreEventListener.class);
    private UpdateScoreDto localUpdateInfo;
    private int operationGate;
    @Resource
    private ScoreService scoreService;

    // 刷新时间间隔阈值（min）
    @Value("${score.update.interval}")
    private Integer interval = -1;
    // 刷新操作数量阈值
    @Value("${score.update.gate}")
    private Integer gate = -1;

    public UpdateScoreEventListener() {
        this.localUpdateInfo = null;
        this.operationGate = -1;
        logger.debug("UpdateScoreEventListener init" + this);
        logger.debug("Interval: " + interval + ", gate: " + gate);
    }

    @EventListener(classes = {UpdateScoreEvent.class})
    @Order(100)
    @Async
    public void eventListener(UpdateScoreEvent event) {
        logger.debug("EventListener is called");
        logger.debug("Interval: " + interval + ", gate: " + gate);
        if (localUpdateInfo == null) {
            localUpdateInfo = event.getUpdateScoreDto();
            logger.debug("First Time's Call.");
            logger.debug(localUpdateInfo.toString());
            updateScores();
        } else {
            UpdateScoreDto updateScoreDto = event.getUpdateScoreDto();
            if ((updateScoreDto.getTime().getTime() - localUpdateInfo.getTime().getTime()) >= 1000 * 60 + interval) {
                logger.info("时间已经超过1min");
            } else {
                if (updateScoreDto.getCount() <= gate) {
                    logger.info("时间未超过1min且数据修改量小于阈值");
                } else {
                    logger.info("时间未超过1min但数据修改量大于阈值");
                }
            }
        }
    }

    public void updateScores() {
        logger.info("Start Update Scores");
        logger.warn("Interval: " + interval + ", gate: " + gate);
        Result<?> result = scoreService.updateAllScores();
        if (result.getCode() != 0) {
            logger.warn("Failed To update Scores");
        } else {
            logger.info("Finish Update Scores");
        }
    }
}
