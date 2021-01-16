package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillConfEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillPayRecordEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserCreditPayBillCreateMessage extends AbsUserActionMessage {
    public UserCreditPayBillCreateMessage() {}

    public UserCreditPayBillCreateMessage(Integer userId, SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity, SwCreditBillConfEntity swCreditBillConfEntity, SwCreditBillPayRecordEntity swCreditBillPayRecordEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.swCreditRecordEntity = swCreditRecordEntity;
        this.swCreditBillConfEntity = swCreditBillConfEntity;
        this.swCreditBillPayRecordEntity = swCreditBillPayRecordEntity;

    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_CREDIT_PAY_BILL;
    private SuBillEntity suBillEntity;
    //用户信用记录
    private SwCreditRecordEntity swCreditRecordEntity;
    //用户当前信用满足的规则
    private SwCreditBillConfEntity swCreditBillConfEntity;
    //用户信用支付实体
    private SwCreditBillPayRecordEntity swCreditBillPayRecordEntity;
}
