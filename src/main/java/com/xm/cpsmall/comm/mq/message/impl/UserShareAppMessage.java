package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import lombok.Data;

@Data
public class UserShareAppMessage extends AbsUserActionMessage {

    public UserShareAppMessage() {}

    public UserShareAppMessage(Integer userId, Integer clickUserId, Integer appType) {
        super(userId);
        this.clickUserId = clickUserId;
        this.appType = appType;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_SHARE_APP;
    //点击用户id
    private Integer clickUserId;
    //app类型
    private Integer appType;
}
