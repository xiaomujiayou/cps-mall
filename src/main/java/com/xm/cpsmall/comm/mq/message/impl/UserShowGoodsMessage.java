package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import lombok.Data;

@Data
public class UserShowGoodsMessage extends AbsUserActionMessage {

    public UserShowGoodsMessage() {}

    public UserShowGoodsMessage(Integer userId, SmProductEntityEx smProductEntity) {
        super(userId);
        this.smProductEntity = smProductEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_SHOW_GOODS;
    //用户浏览一个商品
    private SmProductEntityEx smProductEntity;
}
