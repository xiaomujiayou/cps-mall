package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;


@Data
public class UserAddProxyMessage extends AbsUserActionMessage {

    public UserAddProxyMessage() {}

    public UserAddProxyMessage(Integer userId, Integer level, SuUserEntity proxyUser) {
        super(userId);
        this.level = level;
        this.proxyUser = proxyUser;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_ADD_PROXY;
    //代理级别
    private Integer level;
    //代理用户
    private SuUserEntity proxyUser;
}
