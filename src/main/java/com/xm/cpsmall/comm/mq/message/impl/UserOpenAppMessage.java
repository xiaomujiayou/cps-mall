package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import lombok.Data;

@Data
public class UserOpenAppMessage extends AbsUserActionMessage {

    public UserOpenAppMessage() {}

    public UserOpenAppMessage(Integer userId, Integer appType) {
        super(userId);
        this.appType = appType;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_OPEN_APP;
    //app类型
    private Integer appType;
}
