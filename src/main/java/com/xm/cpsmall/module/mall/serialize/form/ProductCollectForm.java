package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductCollectForm {
    //所属平台
    @NotNull(message = "platformType不能为空")
    private Integer platformType;
    //商品Id
    @NotNull(message = "goodsId不能为空")
    private String goodsId;
    //分享用户
    private Integer shareUserId;
    //取消/收藏
    @NotNull(message = "isCollect不能为空")
    private Boolean isCollect;
}
