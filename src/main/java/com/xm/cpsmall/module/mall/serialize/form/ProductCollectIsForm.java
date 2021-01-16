package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductCollectIsForm {
    //所属平台
    @NotNull(message = "platformType不能为空")
    private Integer platformType;
    //商品Id
    @NotNull(message = "goodsId不能为空")
    private String goodsId;
}
