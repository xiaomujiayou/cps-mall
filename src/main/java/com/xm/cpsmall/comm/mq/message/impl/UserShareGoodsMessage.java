package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import lombok.Data;

@Data
public class UserShareGoodsMessage extends AbsUserActionMessage {

    public UserShareGoodsMessage() {}

    public UserShareGoodsMessage(Integer userId, Integer clickUserId, SmProductEntityEx smProductEntityEx) {
        super(userId);
        this.clickUserId = clickUserId;
        this.smProductEntityEx = smProductEntityEx;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_SHARE_GOODS;
    private Integer clickUserId;
    private SmProductEntityEx smProductEntityEx;
}
