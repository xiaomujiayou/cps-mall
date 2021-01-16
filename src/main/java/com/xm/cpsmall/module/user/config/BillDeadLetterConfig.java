package com.xm.cpsmall.module.user.config;

import com.xm.cpsmall.comm.mq.constant.RabbitMqConstant;
import com.xm.cpsmall.comm.mq.message.config.BillMqConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 账单死信队列的配置
 * 用于用户下单后未支付，延时删除账单
 */
@Configuration
public class BillDeadLetterConfig {

    // 待付款账单队列
    @Bean
    public Queue waittingPayBillQueue(){
        Map<String, Object> args = new HashMap<String, Object>();
        //待支付账单超时时间
        args.put(RabbitMqConstant.TTL_QUEUE_ARG_NAME,10 * 60 * 1000);
        args.put(RabbitMqConstant.DEAD_QUEUE_ARG_EXCHANGE_NAME,BillMqConfig.EXCHANGE);
        args.put(RabbitMqConstant.DEAD_QUEUE_ARG_KEY_NAME,BillMqConfig.KEY_PAY_OVERTIME_DEAD);
        return new Queue(BillMqConfig.QUEUE_PAY_OVERTIME,true,false,false,args);
    }
    @Bean
    public DirectExchange billExchange(){
        return new DirectExchange(BillMqConfig.EXCHANGE);
    }
    // 声明死信队列绑定关系
    @Bean
    public Binding billLetterBinding(Queue waittingPayBillQueue,DirectExchange billExchange){
        return BindingBuilder.bind(waittingPayBillQueue).to(billExchange).with(BillMqConfig.KEY_PAY_OVERTIME);
    }
}