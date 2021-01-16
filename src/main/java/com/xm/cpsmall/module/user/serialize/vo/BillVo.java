package com.xm.cpsmall.module.user.serialize.vo;

import lombok.Data;

@Data
public class BillVo {
    private Integer money;
    private Integer type;
    private Integer state;
    private Integer creditState;
    private Integer income;
    private String time;
    private String failReason;
    private String headImg;
    private String nickname;
    private Integer orderId;
    private String goodsId;
    private String goodsName;
    private Integer platformType;
}
