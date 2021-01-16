package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.wind.constant.ChangeCreditEnum;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserCreditChangeMessage extends AbsUserActionMessage {
    public UserCreditChangeMessage() {}

    public UserCreditChangeMessage(Integer userId, SwCreditRecordEntity oldRecord, SwCreditRecordEntity newRecord, ChangeCreditEnum changeCreditEnum) {
        super(userId);
        this.oldRecord = oldRecord;
        this.newRecord = newRecord;
        this.changeCreditEnum = changeCreditEnum;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_CREDIT_CHANGE;
    //老记录
    private SwCreditRecordEntity oldRecord;
    //新纪录
    private SwCreditRecordEntity newRecord;
    //变更类型
    private ChangeCreditEnum changeCreditEnum;
}
