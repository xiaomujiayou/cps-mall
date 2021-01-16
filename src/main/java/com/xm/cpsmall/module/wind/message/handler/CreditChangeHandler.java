package com.xm.cpsmall.module.wind.message.handler;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.config.WindMqConfig;
import com.xm.cpsmall.comm.mq.message.impl.*;
import com.xm.cpsmall.module.user.constant.BillStateConstant;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.constant.ChangeCreditEnum;
import com.xm.cpsmall.module.wind.constant.CreditConfigEnmu;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditConfEntity;
import com.xm.cpsmall.module.wind.service.CreditBillService;
import com.xm.cpsmall.module.wind.service.UserCreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.xm.cpsmall.comm.mq.constant.RabbitMqConstant.DELAY_MESSAGE_HEAD_NAME;


/**
 * 信用变更
 */
@Slf4j
@Component
public class CreditChangeHandler implements MessageHandler {

    @Autowired
    private CreditBillService creditBillService;
    @Autowired
    private UserCreditService userCreditService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Class> getType() {
        return Arrays.asList(
                UserMaliceCreditBillMessage.class,
                UserAddProxyMessage.class,
                OrderStateChangeMessage.class,
                UserLoginMessage.class,
                UserBillStateChangeMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(message instanceof UserMaliceCreditBillMessage){
            /**
             * 恶意退单消息
             * 降低信用
             */
            UserMaliceCreditBillMessage maliceCreditBillMessage = (UserMaliceCreditBillMessage)message;
            userCreditService.changeCredit(maliceCreditBillMessage.getUserId(), ChangeCreditEnum.ORDER_MALICE,maliceCreditBillMessage.getOldOrder().getOrderSubSn());
        }else if(message instanceof UserAddProxyMessage){
            /**
             * 分享好友奖励
             */
            UserAddProxyMessage addProxyMessage = (UserAddProxyMessage)message;
            if(addProxyMessage.getLevel() == 1)
                userCreditService.changeCredit(addProxyMessage.getUserId(), ChangeCreditEnum.ADD_PROXY,addProxyMessage.getProxyUser().getId().toString());

        }else if(message instanceof UserLoginMessage){
            /**
             * 每日登录奖励
             */
            UserLoginMessage userLoginMessage = (UserLoginMessage)message;
            userCreditService.changeCredit(userLoginMessage.getUserId(), ChangeCreditEnum.LOGIN_DAY, DateUtil.formatDateTime(userLoginMessage.getSuUserEntity().getLastLogin()));
        }else if(message instanceof OrderStateChangeMessage){
            /**
             * 订单完成奖励
             */
            OrderStateChangeMessage orderStateChangeMessage = (OrderStateChangeMessage)message;
            if(orderStateChangeMessage.getOldOrder().getState() == OrderStateConstant.CONFIRM_RECEIPT && orderStateChangeMessage.getNewState() == OrderStateConstant.ALREADY_SETTLED)
                userCreditService.changeCredit(orderStateChangeMessage.getUserId(), ChangeCreditEnum.ORDER_COMPLETE, orderStateChangeMessage.getOldOrder().getProductName());

            /**
             * 订单结束，解除绑定
             */
            if(orderStateChangeMessage.getNewState() == OrderStateConstant.ALREADY_SETTLED || orderStateChangeMessage.getNewState() == OrderStateConstant.FAIL_SETTLED){
                if(orderStateChangeMessage.getSuBillEntity() != null){
                    creditBillService.creditUnBindBill(
                            orderStateChangeMessage.getSuBillEntity(),
                            creditBillService.getUserCredit(orderStateChangeMessage.getUserId()),
                            1,
                            "订单结束");
                }
            }
        }else if(message instanceof UserBillStateChangeMessage){
            /**
             * 打款后提前解绑账单
             */
            UserBillStateChangeMessage stateChangeMessage = (UserBillStateChangeMessage)message;
            SwCreditConfEntity creditConfEntity = creditBillService.getConfig(CreditConfigEnmu.DEFAULT_CREDIT_UNBIND_DELAY);
            if(stateChangeMessage.getNewState() == BillStateConstant.ALREADY){
                SuBillEntity suBillEntity = stateChangeMessage.getOldBill();
                suBillEntity.setState(stateChangeMessage.getNewState());
                rabbitTemplate.convertAndSend(WindMqConfig.EXCHANGE_DELAY, WindMqConfig.KEY_CREDIT_UNBIND_DELAY,suBillEntity, msg -> {
                    // 设置消息属性-过期时间
                    Date start = DateUtil.date();
                    Date end = DateUtil.date().offset(DateField.HOUR_OF_DAY,24 * Integer.valueOf(creditConfEntity.getVal()));
                    msg.getMessageProperties().setHeader(DELAY_MESSAGE_HEAD_NAME,DateUtil.between(start,end, DateUnit.MS));
                    return msg;
                });
            }
        }
    }

    @Override
    public void onError(Exception e) {

    }
}

