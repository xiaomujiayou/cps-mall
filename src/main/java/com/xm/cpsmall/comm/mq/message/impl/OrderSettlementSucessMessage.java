package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderSettlementSucessMessage extends AbsUserActionMessage {

    public OrderSettlementSucessMessage() {}

    public OrderSettlementSucessMessage(Integer userId, SuOrderEntity suOrderEntity) {
        super(userId);
        this.suOrderEntity = suOrderEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.ORDER_SETTLEMENT_SUCESS;
    //相关订单
    private SuOrderEntity suOrderEntity;
}
