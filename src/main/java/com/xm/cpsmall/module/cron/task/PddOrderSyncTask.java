package com.xm.cpsmall.module.cron.task;

import com.xm.cpsmall.module.cron.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 同步拼多多订单
 */
@Slf4j
@Component
public class PddOrderSyncTask{

    @Resource(name = "pddTaskService")
    private TaskService pddTaskService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void dowork() {
        pddTaskService.start();
    }
}
