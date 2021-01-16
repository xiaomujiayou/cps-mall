package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.cron.serialize.entity.ScBillPayEntity;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxEntPayOrderInEntity;
import lombok.Data;

@Data
public class UserPaymentSucessMessage extends AbsUserActionMessage {
    public UserPaymentSucessMessage(){}
    public UserPaymentSucessMessage(Integer userId, ScBillPayEntity scBillPayEntity, SpWxEntPayOrderInEntity spWxEntPayOrderInEntity) {
        super(userId);
        this.scBillPayEntity = scBillPayEntity;
        this.spWxEntPayOrderInEntity = spWxEntPayOrderInEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_PAYMENT_SUCESS;
    private SpWxEntPayOrderInEntity spWxEntPayOrderInEntity;
    private ScBillPayEntity scBillPayEntity;
}
