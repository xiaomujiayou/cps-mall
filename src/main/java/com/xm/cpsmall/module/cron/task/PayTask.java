package com.xm.cpsmall.module.cron.task;

import com.xm.cpsmall.module.cron.service.BillPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 付款任务
 */
@Slf4j
@Component
public class PayTask {

    @Autowired
    private BillPayService billPayService;

    //每天晚上9点
    @Scheduled(cron = "0 0 21 * * ?")
    public void dowork() {
        log.debug("开始放工资");
        billPayService.commission();
        log.debug("工资发放结束");
    }
}
