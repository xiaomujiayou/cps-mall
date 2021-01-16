package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import lombok.Data;

@Data
public class UserBillCommissionFailSucessMessage extends AbsUserActionMessage {

    public UserBillCommissionFailSucessMessage() {}

    public UserBillCommissionFailSucessMessage(Integer userId, SuBillEntity suBillEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_BILL_COMMISSION_FAIL;
    private SuBillEntity suBillEntity;
    private String failReason;
}
