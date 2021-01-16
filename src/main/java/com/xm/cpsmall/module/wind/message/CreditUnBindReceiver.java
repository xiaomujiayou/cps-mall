package com.xm.cpsmall.module.wind.message;

import com.rabbitmq.client.Channel;
import com.xm.cpsmall.comm.mq.message.config.WindMqConfig;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.service.CreditBillService;
import com.xm.cpsmall.module.wind.service.UserCreditService;
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
public class CreditUnBindReceiver {

    @Autowired
    private CreditBillService creditBillService;
    @Autowired
    private UserCreditService userCreditService;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(
                    value = WindMqConfig.EXCHANGE_DELAY,
                    type = DELAY_EXCHANGE_TYPE,
                    arguments = @Argument(name = DELAY_EXCHANGE_TARGET_TYPE,value = ExchangeTypes.DIRECT)),
            key = WindMqConfig.KEY_CREDIT_UNBIND_DELAY,
            value = @Queue(value = WindMqConfig.QUEUE_CREDIT_UNBIND_DELAY)
    ))
    public void onMessage(SuBillEntity suBillEntity, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            creditBillService.creditUnBindBill(
                    suBillEntity,
                    creditBillService.getUserCredit(suBillEntity.getUserId()),
                    1,
                    "信用打款后10天无退款，提现解绑");
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 信用账单提前解除绑定失败 帐单号：{} 异常：{}",msgId,suBillEntity.getBillSn(),e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }
}
