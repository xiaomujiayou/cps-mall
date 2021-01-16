package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import lombok.Data;

@Data
public class UserClickGoodsMessage extends AbsUserActionMessage {

    public UserClickGoodsMessage() {}

    public UserClickGoodsMessage(Integer userId, Integer fromUserId,String ip,Integer appType, SmProductEntityEx smProductEntityEx) {
        super(userId);
        this.fromUserId = fromUserId;
        this.smProductEntityEx = smProductEntityEx;
        this.appType = appType;
        this.ip = ip;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_CLICK_GOODS;
    private Integer fromUserId;
    private Integer appType;
    private String ip;
    private SmProductEntityEx smProductEntityEx;
}
