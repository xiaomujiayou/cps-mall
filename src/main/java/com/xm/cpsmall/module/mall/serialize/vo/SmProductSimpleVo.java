package com.xm.cpsmall.module.mall.serialize.vo;

import lombok.Data;

@Data
public class SmProductSimpleVo {
    private Integer type;
    private String goodsId;
    private String goodsThumbnailUrl;
    private String name;
    private Integer originalPrice;
}
