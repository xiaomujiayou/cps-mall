package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;


@Data
public class UserFristLoginMessage extends AbsUserActionMessage {

    public UserFristLoginMessage() {}

    public UserFristLoginMessage(Integer userId, SuUserEntity userEntity) {
        super(userId);
        this.userEntity = userEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_FRIST_LOGIN;
    //用户信息
    private SuUserEntity userEntity;
}
