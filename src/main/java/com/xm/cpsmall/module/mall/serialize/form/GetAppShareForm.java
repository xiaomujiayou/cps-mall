package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetAppShareForm {
    //分享类型(1:app分享,2:商品详情分享)
    @NotNull(message = "type不能为空")
    private Integer type;

    private String goodsId;
}
