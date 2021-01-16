package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;

@Data
public class UserLoginMessage extends AbsUserActionMessage {

    public UserLoginMessage(){}

    public UserLoginMessage(Integer userId, SuUserEntity suUserEntity) {
        super(userId);
        this.suUserEntity = suUserEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_LOGIN;
    private SuUserEntity suUserEntity;
}
