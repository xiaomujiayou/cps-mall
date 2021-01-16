package com.xm.cpsmall.module.cron.task;

import com.xm.cpsmall.comm.api.client.MyMogujieClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 蘑菇街token刷新任务
 */
@Slf4j
@Component
public class MgjTokenRefreshTask {

    @Autowired
    private MyMogujieClient myMogujieClient;

    /**
     * 每天早上十点刷新
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void refreshToken() {
        log.info("蘑菇街Token刷新：{}",myMogujieClient.refreshToken());
    }
}
