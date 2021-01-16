package com.xm.cpsmall.comm.mq.message.config;

import org.springframework.amqp.core.ExchangeTypes;

/**
 * 用户动态队列配置
 */
public class UserActionConfig {
    public static final String EXCHANGE = "user.action";
    public static final String EXCHANGE_TYPE = ExchangeTypes.FANOUT;
    //抽奖f服务队列
    public static final String QUEUE_LOTTERY = EXCHANGE + ".lottery.queue";
    //用户服务队列
    public static final String QUEUE_USER = EXCHANGE + ".user.queue";
    //用户服务队列
    public static final String QUEUE_MALL = EXCHANGE + ".mall.queue";
    //定时任务队列
    public static final String QUEUE_CRON = EXCHANGE + ".cron.queue";
    //风控任务队列
    public static final String QUEUE_WIND = EXCHANGE + ".wind.queue";
    //风控任务队列
    public static final String QUEUE_ACTIVE = EXCHANGE + ".active.queue";

}
