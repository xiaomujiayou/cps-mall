package com.xm.cpsmall.comm.mq.constant;

public class RabbitMqConstant {

    public static final String DEAD_QUEUE_ARG_EXCHANGE_NAME = "x-dead-letter-exchange";

    public static final String DEAD_QUEUE_ARG_KEY_NAME = "x-dead-letter-routing-key";

    public static final String TTL_QUEUE_ARG_NAME = "x-message-ttl";
    //延时插件 延时队列类型
    public static final String DELAY_EXCHANGE_TARGET_TYPE = "x-delayed-type";
    //延时插件 延时队列交换机类型
    public static final String DELAY_EXCHANGE_TYPE = "x-delayed-message";
    //延时插件 延时队列类型
    public static final String DELAY_MESSAGE_HEAD_NAME = "x-delay";


}
