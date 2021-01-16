package com.xm.cpsmall.comm.mq.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xm.cpsmall.comm.mq.handler.MessageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


/**
 * 用户动态消息统一处理器
 * 用到该模块的服务应初始化MessageManager
 */
@Slf4j
public abstract class AbsMessageReceiver {

    @Autowired
    private MessageManager messageManager;

    public void onMessage(JSONObject jsonMessage, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            UserActionEnum userActionEnum = jsonMessage.getObject("userActionEnum",UserActionEnum.class);
            log.debug("user action:{} data:{}",userActionEnum.getName(), JSON.toJSONString(jsonMessage));
            AbsUserActionMessage absUserActionMessage = (AbsUserActionMessage) jsonMessage.toJavaObject(userActionEnum.getMessageType());
            messageManager.handleMessage(absUserActionMessage);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("user action error：{} msg：{} 处理失败 error：{}",msgId,jsonMessage,e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }
}
