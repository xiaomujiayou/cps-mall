package com.xm.cpsmall.comm.mq.message.config;

public class OrderMqConfig {

    //待处理的原始账单
    public static final String EXCHANGE = "order";
    public static final String KEY = "original";
    public static final String QUEUE = EXCHANGE  + "." + KEY + ".queue";

    //处理失败的订单队列
    public static final String KEY_PROCESS_FAIL = EXCHANGE  + ".process.fail";
    public static final String QUEUE_PROCESS_FAIL = KEY_PROCESS_FAIL  + ".queue";

}
