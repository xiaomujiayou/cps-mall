package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class UserMaliceCreditBillMessage extends AbsUserActionMessage {

    public UserMaliceCreditBillMessage() {}

    public UserMaliceCreditBillMessage(Integer userId, SuBillEntity suBillEntity, SuOrderEntity oldOrder, SuOrderEntity newOrder) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_MALICE_CREDIT_BILL;
    private SuBillEntity suBillEntity;
    private SuOrderEntity oldOrder;
    private SuOrderEntity newOrder;
}
