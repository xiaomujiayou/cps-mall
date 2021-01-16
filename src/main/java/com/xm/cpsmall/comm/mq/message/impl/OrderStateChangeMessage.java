package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderStateChangeMessage extends AbsUserActionMessage {

    public OrderStateChangeMessage() {}

    public OrderStateChangeMessage(Integer userId, SuOrderEntity oldOrder, SuOrderEntity newOrder, SuBillEntity suBillEntity) {
        super(userId);
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
        this.newState = newOrder.getState();
        this.suBillEntity = suBillEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.ORDER_STATE_CHANGE;
    //相关订单
    private SuOrderEntity oldOrder;
    private SuOrderEntity newOrder;
    //相关账单（购买者账单）
    private SuBillEntity suBillEntity;
    //新的状态(OrderStateContanst)
    private Integer newState;
}
