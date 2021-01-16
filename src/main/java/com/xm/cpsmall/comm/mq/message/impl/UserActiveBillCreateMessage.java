package com.xm.cpsmall.comm.mq.message.impl;


import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import lombok.Data;

@Data
public class UserActiveBillCreateMessage extends AbsUserActionMessage {

    public UserActiveBillCreateMessage() {}

    public UserActiveBillCreateMessage(Integer userId, SaBillEntity saBillEntity) {
        super(userId);
        this.saBillEntity = saBillEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_ACTIVE_BILL_CREATE;
    private SaBillEntity saBillEntity;
}
