package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserUnBindCreditBillMessage extends AbsUserActionMessage {

    public UserUnBindCreditBillMessage() {}

    public UserUnBindCreditBillMessage(Integer userId, SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity, Integer unBindType, String unBindReason) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.swCreditRecordEntity = swCreditRecordEntity;
        this.unBindType = unBindType;
        this.unBindReason = unBindReason;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_UN_BIND_CREDIT_BILL;

    private SuBillEntity suBillEntity;
    private SwCreditRecordEntity swCreditRecordEntity;
    private Integer unBindType;
    private String unBindReason;
}
