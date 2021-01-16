package com.xm.cpsmall.module.user.message.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserCreditBillCountDownMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserCreditPayBillCreateMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserUnBindCreditBillMessage;
import com.xm.cpsmall.module.user.mapper.SuBillMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.constant.CreditBillUnbindConstant;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillPayRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设置账单信用支付状态
 */
@Slf4j
@Component
public class CreditBillHandler implements MessageHandler {

    @Autowired
    private SuBillMapper suBillMapper;

    @Override
    public List<Class> getType() {
        return Lists.newArrayList(
                UserCreditPayBillCreateMessage.class,
                UserCreditBillCountDownMessage.class,
                UserUnBindCreditBillMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(message instanceof UserCreditPayBillCreateMessage) {
            /**
             * 信用账单已建立
             */
            UserCreditPayBillCreateMessage creditPayBillCreateMessage = (UserCreditPayBillCreateMessage) message;
            //重复检测
            SuBillEntity record = creditPayBillCreateMessage.getSuBillEntity();
            if (record == null || record.getId() == null){
                log.warn("UserCreditPayBillCreateMessage 中包含的 SuBillEntity 不存在：message:{}", JSON.toJSONString(creditPayBillCreateMessage));
                return;
            }
            SuBillEntity suBillEntity = suBillMapper.selectByPrimaryKey(creditPayBillCreateMessage.getSuBillEntity().getId());
            for (int i = 0; i < 5; i++) {
                if(suBillEntity == null) {
                    try {
                        Thread.sleep(1000);
                        suBillEntity = suBillMapper.selectByPrimaryKey(creditPayBillCreateMessage.getSuBillEntity().getId());
                        if(suBillEntity != null)
                            break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (suBillEntity == null || suBillEntity.getCreditState() != null){
                log.warn("重复消息 UserCreditPayBillCreateMessage 消息已被处理：message:{}", JSON.toJSONString(creditPayBillCreateMessage));
                return;
            }
            //设置账单信用支付状态
            SwCreditBillPayRecordEntity billPayRecordEntity = creditPayBillCreateMessage.getSwCreditBillPayRecordEntity();
            if (billPayRecordEntity != null) {
                suBillEntity.setCreditState(billPayRecordEntity.getCheckResult());
                suBillMapper.updateByPrimaryKeySelective(suBillEntity);
            }else {
                log.warn("无效 UserCreditPayBillCreateMessage 消息中 SwCreditBillPayRecordEntity 不存在：message:{}", JSON.toJSONString(creditPayBillCreateMessage));
            }
        }else if(message instanceof UserCreditBillCountDownMessage){
            /**
             * 设置信用支付付款时间
             */
            UserCreditBillCountDownMessage creditBillCountDownMessage = (UserCreditBillCountDownMessage)message;
            SuBillEntity suBillEntity = creditBillCountDownMessage.getSuBillEntity();
            SuBillEntity record = suBillMapper.selectByPrimaryKey(suBillEntity.getId());
            if(record.getPayTime() != null)
                return;
            SuBillEntity bill = new SuBillEntity();
            bill.setId(record.getId());
            bill.setPayTime(suBillEntity.getPayTime());
            suBillMapper.updateByPrimaryKeySelective(bill);
        }else if(message instanceof UserUnBindCreditBillMessage){
            /**
             * 信用降低解绑一个账单
             */
            UserUnBindCreditBillMessage unBindCreditBillMessage = (UserUnBindCreditBillMessage)message;
            if(unBindCreditBillMessage.getUnBindType() == CreditBillUnbindConstant.CREDIT_REDUCE){
                //信用降低，信用解绑
                SuBillEntity suBillEntity = new SuBillEntity();
                suBillEntity.setId(unBindCreditBillMessage.getSuBillEntity().getId());
                suBillEntity.setCreditState(2);
                suBillMapper.updateByPrimaryKeySelective(suBillEntity);
            }
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
