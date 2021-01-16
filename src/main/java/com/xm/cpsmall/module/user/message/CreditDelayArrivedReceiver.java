package com.xm.cpsmall.module.user.message;

import com.rabbitmq.client.Channel;
import com.xm.cpsmall.comm.mq.message.config.WindMqConfig;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.service.UserBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.xm.cpsmall.comm.mq.constant.RabbitMqConstant.DELAY_EXCHANGE_TARGET_TYPE;
import static com.xm.cpsmall.comm.mq.constant.RabbitMqConstant.DELAY_EXCHANGE_TYPE;

/**
 * 信用支付账单延时已完成
 */
@Slf4j
@Component
public class CreditDelayArrivedReceiver {

    @Autowired
    private UserBillService userBillService;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(
                    value = WindMqConfig.EXCHANGE_DELAY,
                    type = DELAY_EXCHANGE_TYPE,
                    arguments = @Argument(name = DELAY_EXCHANGE_TARGET_TYPE,value = ExchangeTypes.DIRECT)),
            key = WindMqConfig.KEY_CREDIT_DELAY,
            value = @Queue(value = WindMqConfig.QUEUE_CREDIT_DELAY)
    ))
    public void onMessage(SuBillEntity suBillEntity, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            userBillService.onCreditDelayArrived(suBillEntity);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 信用账单延时完成失败 单号：{} 异常：{}",msgId,suBillEntity.getBillSn(),e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }
}
