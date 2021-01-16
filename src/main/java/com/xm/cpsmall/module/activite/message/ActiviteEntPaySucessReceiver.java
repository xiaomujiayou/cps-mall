package com.xm.cpsmall.module.activite.message;

import com.rabbitmq.client.Channel;
import com.xm.cpsmall.comm.mq.message.config.PayMqConfig;
import com.xm.cpsmall.module.activite.service.ActiveService;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxEntPayOrderInEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ActiviteEntPaySucessReceiver {

    @Autowired
    private ActiveService activeService;

    /**
     * 活动提现执行结果
     * @param spWxEntPayOrderInEntity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = PayMqConfig.EXCHANGE_ENT,type = PayMqConfig.EXCHANGE_ENT_TYPE),
            value = @Queue(PayMqConfig.QUEUE_ENT_PAY_ACTIVE_SUCESS)
    ))
    public void onSucessMessage(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            if(spWxEntPayOrderInEntity.getType() == 2)
                activeService.onCashoutEntPayResult(spWxEntPayOrderInEntity);
            if(spWxEntPayOrderInEntity.getType() == 3)
                activeService.onCashoutAutoEntPayResult(spWxEntPayOrderInEntity);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 企业付款失败 处理失败 billPayId：{} wxEntPayId：{} error：{}",msgId,spWxEntPayOrderInEntity.getBillPayId(),spWxEntPayOrderInEntity.getId(),e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }
}
