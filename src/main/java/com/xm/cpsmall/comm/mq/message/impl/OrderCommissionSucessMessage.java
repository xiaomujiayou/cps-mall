package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderCommissionSucessMessage extends AbsUserActionMessage {

    public OrderCommissionSucessMessage() {}

    public OrderCommissionSucessMessage(Integer userId, SuOrderEntity suOrderEntity, SuBillEntity suBillEntity) {
        super(userId);
        this.suOrderEntity = suOrderEntity;
        this.suBillEntity = suBillEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.ORDER_COMMISSION_SUCESS;
    //相关订单
    private SuOrderEntity suOrderEntity;
    //相关账单
    private SuBillEntity suBillEntity;
}
