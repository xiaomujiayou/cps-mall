package com.xm.cpsmall.module.pay.config;

import com.xm.cpsmall.comm.mq.message.config.PayMqConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付死信队列的配置
 * 用于订单处理失败
 */
@Configuration
public class PayDeadLetterConfig {

    // 声明死信队列
    @Bean
    public Queue wxPayLetterQueue(){
        return new Queue(PayMqConfig.QUEUE_WX_NOTIFY_FAIL,true);
    }

    // 声明死信队列绑定关系
    @Bean
    public Binding wxPayLetterBinding(Queue wxPayLetterQueue){
        return BindingBuilder.bind(wxPayLetterQueue).to(new DirectExchange(PayMqConfig.EXCHANGE)).with(PayMqConfig.KEY_WX_NOTIFY_FAIL);
    }
}