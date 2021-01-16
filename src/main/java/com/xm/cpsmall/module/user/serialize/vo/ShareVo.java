package com.xm.cpsmall.module.user.serialize.vo;

import lombok.Data;

@Data
public class ShareVo {
    private Integer id;
    private String goodsId;
    private Integer platformType;
    private String goodsImg;
    private String title;
    private Integer originalPrice;
    private Integer coupon;
    private Integer red;
    private Integer shareMoney;
    private Integer show;
    private Integer sellOut;
    private Integer willMakeMoney;
    private String salesTip;
    private String createTime;
}
