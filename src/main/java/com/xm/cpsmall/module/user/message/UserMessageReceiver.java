package com.xm.cpsmall.module.user.message;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xm.cpsmall.comm.mq.message.AbsMessageReceiver;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听用户动态
 */
@Slf4j
@Component
public class UserMessageReceiver extends AbsMessageReceiver {

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = UserActionConfig.EXCHANGE, type = UserActionConfig.EXCHANGE_TYPE),
            value = @Queue(UserActionConfig.QUEUE_USER)
    ))
    @Override
    public void onMessage(JSONObject jsonMessage, Channel channel, Message message) throws IOException {
        super.onMessage(jsonMessage, channel, message);
    }

}
