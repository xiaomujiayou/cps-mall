package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxOrderNotifyEntity;
import com.xm.cpsmall.module.user.serialize.bo.SuBillToPayBo;
import lombok.Data;

@Data
public class PayOrderSucessMessage extends AbsUserActionMessage {

    public PayOrderSucessMessage() {}


    public PayOrderSucessMessage(Integer userId, SuBillToPayBo suBillToPayBo, SpWxOrderNotifyEntity spWxOrderNotifyEntity) {
        super(userId);
        this.suBillToPayBo = suBillToPayBo;
        this.spWxOrderNotifyEntity = spWxOrderNotifyEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.PAY_ORDER_NOTIFY_SUCESS;
    private SuBillToPayBo suBillToPayBo;
    private SpWxOrderNotifyEntity spWxOrderNotifyEntity;
}
