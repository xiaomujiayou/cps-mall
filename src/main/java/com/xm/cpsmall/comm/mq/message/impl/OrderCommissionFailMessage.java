package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderCommissionFailMessage extends AbsUserActionMessage {
    public OrderCommissionFailMessage() {}

    public OrderCommissionFailMessage(Integer userId, SuOrderEntity suOrderEntity, SuBillEntity suBillEntity, String failReason) {
        super(userId);
        this.suOrderEntity = suOrderEntity;
        this.suBillEntity = suBillEntity;
        this.failReason = failReason;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.ORDER_COMMISSION_FAIL;
    //相关订单
    private SuOrderEntity suOrderEntity;
    //相关账单
    private SuBillEntity suBillEntity;
    //失败原因
    private String failReason;


}
