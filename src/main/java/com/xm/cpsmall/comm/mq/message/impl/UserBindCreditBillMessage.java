package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillBindRecordEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillConfEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillPayRecordEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserBindCreditBillMessage extends AbsUserActionMessage {

    public UserBindCreditBillMessage(){}

    public UserBindCreditBillMessage(Integer userId, SwCreditBillBindRecordEntity swCreditBillBindRecordEntity, SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity, SwCreditBillConfEntity swCreditBillConfEntity, SwCreditBillPayRecordEntity swCreditBillPayRecordEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.swCreditBillBindRecordEntity = swCreditBillBindRecordEntity;
        this.swCreditRecordEntity = swCreditRecordEntity;
        this.swCreditBillConfEntity = swCreditBillConfEntity;
        this.swCreditBillPayRecordEntity = swCreditBillPayRecordEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_BIND_CREDIT_BILL;

    private SuBillEntity suBillEntity;
    private SwCreditBillBindRecordEntity swCreditBillBindRecordEntity;
    private SwCreditRecordEntity swCreditRecordEntity;
    private SwCreditBillConfEntity swCreditBillConfEntity;
    private SwCreditBillPayRecordEntity swCreditBillPayRecordEntity;
}
