package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderSettlementFailMessage extends AbsUserActionMessage {

    public OrderSettlementFailMessage() {}

    public OrderSettlementFailMessage(Integer userId, SuOrderEntity suOrderEntity, String failReason) {
        super(userId);
        this.suOrderEntity = suOrderEntity;
        this.failReason = failReason;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.ORDER_SETTLEMENT_FAIL;
    //相关订单
    private SuOrderEntity suOrderEntity;
    //失败原因
    private String failReason;
}
