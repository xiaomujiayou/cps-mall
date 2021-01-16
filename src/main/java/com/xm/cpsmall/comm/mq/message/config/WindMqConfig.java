package com.xm.cpsmall.comm.mq.message.config;

public class WindMqConfig {

    //风控系统API监听队列
    public static final String EXCHANGE = "wind";
    public static final String KEY_API = "api";
    public static final String QUEUE_API = EXCHANGE  + "." + KEY_API + ".queue";

    //用户登录队列
    public static final String KEY_LOGIN =  "login";
    public static final String QUEUE_LOGIN = EXCHANGE + "." + KEY_LOGIN  + ".queue";

    //信用支付延时队列
    public static final String EXCHANGE_DELAY = "wind.delay";
    public static final String KEY_CREDIT_DELAY =  "credit";
    public static final String QUEUE_CREDIT_DELAY = EXCHANGE_DELAY + "." + KEY_CREDIT_DELAY  + ".queue";
    //信用提前解绑
    public static final String KEY_CREDIT_UNBIND_DELAY =  "credit.unbind";
    public static final String QUEUE_CREDIT_UNBIND_DELAY = EXCHANGE_DELAY + "." + KEY_CREDIT_UNBIND_DELAY  + ".queue";
}
