package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import lombok.Data;

@Data
public class UserSearchGoodsMessage extends AbsUserActionMessage {

    public UserSearchGoodsMessage() {}

    public UserSearchGoodsMessage(Integer userId, Integer platformType, String keywords, Integer pageNum) {
        super(userId);
        this.platformType = platformType;
        this.keywords = keywords;
        this.pageNum = pageNum;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_SEARCH_GOODS;
    //平台类型
    private Integer platformType;
    //搜索关键字
    private String keywords;
    //当前页
    private Integer pageNum;
}
