package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class UserBillCreateMessage extends AbsUserActionMessage {
    public UserBillCreateMessage() {}

    public UserBillCreateMessage(Integer userId, SuBillEntity suBillEntity, SuOrderEntity suOrderEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.suOrderEntity = suOrderEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_BILL_CREATE;
    private SuBillEntity suBillEntity;
    //账单所属订单
    private SuOrderEntity suOrderEntity;
}
