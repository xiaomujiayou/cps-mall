package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SimilarGoodsListForm extends GoodsListForm {
    @NotNull(message = "goodsId 不能为空")
    private String goodsId;
}
