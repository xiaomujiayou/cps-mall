package com.xm.cpsmall.comm.mq.message.config;

public class BillMqConfig {

    //待返现账单队列
    public static final String EXCHANGE = "bill";
    public static final String KEY = "sucess";
    public static final String QUEUE = EXCHANGE + "." + KEY + ".queue";

    //支付成功账单队列
    public static final String KEY_PAY_SUCESS = "pay.sucess";
    public static final String QUEUE_PAY_SUCESS = EXCHANGE + "." + KEY_PAY_SUCESS + ".queue";

    //死信队列(账单支付超时)
    public static final String KEY_PAY_OVERTIME = "pay.overtime";
    public static final String QUEUE_PAY_OVERTIME = EXCHANGE + "." + KEY_PAY_OVERTIME + ".queue";
    public static final String KEY_PAY_OVERTIME_DEAD = "pay.overtime.dead";
    public static final String QUEUE_PAY_OVERTIME_DEAD = EXCHANGE + "." + KEY_PAY_OVERTIME_DEAD + ".queue";

}
