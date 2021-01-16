package com.xm.cpsmall.module.pay.message;

import com.rabbitmq.client.Channel;
import com.xm.cpsmall.comm.mq.constant.RabbitMqConstant;
import com.xm.cpsmall.comm.mq.message.config.PayMqConfig;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxOrderNotifyEntity;
import com.xm.cpsmall.module.pay.serialize.message.ActiveAutoEntPayMessage;
import com.xm.cpsmall.module.pay.serialize.message.ActiveEntPayMessage;
import com.xm.cpsmall.module.pay.serialize.message.EntPayMessage;
import com.xm.cpsmall.module.pay.service.WxPayApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class WxPayReceiver {

    @Autowired
    private WxPayApiService wxPayApiService;

    /**
     * 微信支付回调
     * @param spWxOrderNotifyEntity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(PayMqConfig.EXCHANGE),
            key = PayMqConfig.KEY_WX_NOTIFY,
            value = @Queue(value = PayMqConfig.QUEUE_WX_NOTIFY,
                    arguments = {
                            @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_EXCHANGE_NAME,value = PayMqConfig.EXCHANGE),
                            @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_KEY_NAME,value = PayMqConfig.KEY_WX_NOTIFY_FAIL)
                    })
    ))
    public void onNotifyMessage(SpWxOrderNotifyEntity spWxOrderNotifyEntity, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            wxPayApiService.onPaySucess(spWxOrderNotifyEntity);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 单号：{} 处理失败 error：{}",msgId,spWxOrderNotifyEntity.getOutTradeNo(),e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }


    /**
     * 订单返现付款
     * @param entPayMessage
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(PayMqConfig.EXCHANGE),
            key = PayMqConfig.KEY_WX_ENT_PAY,
            value = @Queue(value = PayMqConfig.QUEUE_WX_ENT_PAY)
    ))
    public void onEntPayMessage(EntPayMessage entPayMessage, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            wxPayApiService.payment(entPayMessage);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 微信支付企业付款 处理失败 error：{}",msgId,e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }

    /**
     * 活动提现付款
     * @param activeEntPayMessage
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(PayMqConfig.EXCHANGE),
            key = PayMqConfig.KEY_WX_ENT_PAY_ACTIVE,
            value = @Queue(value = PayMqConfig.QUEUE_WX_ENT_PAY_ACTIVE)
    ))
    public void onActiveEntPayMessage(ActiveEntPayMessage activeEntPayMessage, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            wxPayApiService.paymentActive(activeEntPayMessage);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 微信支付企业付款 处理失败 error：{}",msgId,e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }

    /**
     * 活动直接付款
     * @param activeAutoEntPayMessage
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(PayMqConfig.EXCHANGE),
            key = PayMqConfig.KEY_WX_ENT_PAY_ACTIVE_AUTO,
            value = @Queue(value = PayMqConfig.QUEUE_WX_ENT_PAY_ACTIVE_AUTO)
    ))
    public void onActiveEntPayMessage(ActiveAutoEntPayMessage activeAutoEntPayMessage, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            wxPayApiService.paymentActiveAuto(activeAutoEntPayMessage);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 微信支付企业付款 处理失败 error：{}",msgId,e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }



}
